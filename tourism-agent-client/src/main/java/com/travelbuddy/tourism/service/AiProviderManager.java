package com.travelbuddy.tourism.service;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.beans.factory.annotation.Value;

@Service 
@Slf4j
public class AiProviderManager {

    public enum Provider {
        GROQ, GEMINI
    }

    @Getter
    private final AtomicReference<Provider> activeProvider = new AtomicReference<>(Provider.GEMINI);

    private final ChatModel groqChatModel;
    private final ChatModel geminiChatModel;

    private final AtomicLong lastGeminiFailureTime = new AtomicLong(0);

    @Value("${ai.failover.recovery-timeout-ms:300000}")
    private long minRecoveryWaitMs;

    @Autowired
    public AiProviderManager(@Lazy ChatModel groqChatModel, @Lazy ChatModel geminiChatModel) {
        this.groqChatModel = groqChatModel;
        this.geminiChatModel = geminiChatModel;
    }

    @PostConstruct
    public void init() {
        checkProviders();
    }

    public void checkProviders() {
        log.info("Performing startup health check for AI providers...");
        
        boolean geminiOk = isProviderHealthy(geminiChatModel, "Gemini");
        if (geminiOk) {
            log.info("Gemini is healthy. Setting as primary provider.");
            activeProvider.set(Provider.GEMINI);
        } else {
            log.warn("Gemini health check failed at startup. Falling back to Groq.");
            lastGeminiFailureTime.set(System.currentTimeMillis());
            boolean groqOk = isProviderHealthy(groqChatModel, "Groq");
            if (groqOk) {
                log.info("Groq is healthy. Setting as secondary provider.");
                activeProvider.set(Provider.GROQ);
            } else {
                log.error("All AI providers health check failed! Defaulting to Gemini for later retry.");
                activeProvider.set(Provider.GEMINI);
            }
        }
    }

    /**
     * Background task to check Gemini health and switch back if it recovers.
     * This avoids latency spikes for end-users.
     */
    @Scheduled(fixedDelayString = "${ai.failover.check-interval-ms:60000}")
    public void performBackgroundRecoveryCheck() {
        if (activeProvider.get() == Provider.GROQ) {
            long timeSinceFailure = System.currentTimeMillis() - lastGeminiFailureTime.get();
            if (timeSinceFailure > minRecoveryWaitMs) {
                log.info("Background Check: Attempting to see if Gemini has recovered...");
                if (isProviderHealthy(geminiChatModel, "Gemini")) {
                    log.info("Gemini RECOVERED! Switching primary back to Gemini.");
                    activeProvider.set(Provider.GEMINI);
                } else {
                    log.debug("Gemini still unavailable. Staying with Groq.");
                    // Reset failure time to wait another period before checking again
                    lastGeminiFailureTime.set(System.currentTimeMillis());
                }
            }
        }
    }

    private boolean isProviderHealthy(ChatModel model, String name) {
        try {
            // Minimal request to verify health
            model.chat("hi"); 
            return true;
        } catch (Exception e) {
            log.warn("{} health check diagnostic failure: {}", name, e.getMessage());
            return false;
        }
    }

    public void switchToNextProvider() {
        Provider current = activeProvider.get();
        if (current == Provider.GEMINI) {
            log.warn("Gemini failed during request. Switching to Groq fallback.");
            lastGeminiFailureTime.set(System.currentTimeMillis());
            activeProvider.set(Provider.GROQ);
        } else {
            log.info("Groq failed during request. Attempting immediate switch to Gemini.");
            activeProvider.set(Provider.GEMINI);
        }
    }

    public ChatModel getActiveModel() {
        return activeProvider.get() == Provider.GROQ ? groqChatModel : geminiChatModel;
    }
}
