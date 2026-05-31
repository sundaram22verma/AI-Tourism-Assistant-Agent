package com.travelbuddy.tourism.model;

import com.travelbuddy.tourism.service.AiProviderManager;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FailoverChatModel implements ChatModel {

    private final AiProviderManager providerManager;

    public FailoverChatModel(AiProviderManager providerManager) {
        this.providerManager = providerManager;
    }

    @Override
    public ChatResponse chat(ChatRequest chatRequest) {
        ChatModel model = providerManager.getActiveModel();
        String providerName = (providerManager.getActiveProvider().get() == AiProviderManager.Provider.GEMINI) ? "Gemini" : "Groq";
        
        try {
            log.debug("Attempting AI request with {} provider", providerName);
            return model.chat(chatRequest);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (isRetryableError(msg)) {
                log.warn("{} AI provider failed (Error: {}). Attempting failover...", providerName, msg);
                
                // Switch provider
                providerManager.switchToNextProvider();
                
                // Get the new model (Groq if Gemini failed, or Gemini if Groq failed)
                ChatModel fallbackModel = providerManager.getActiveModel();
                String fallbackName = (providerManager.getActiveProvider().get() == AiProviderManager.Provider.GEMINI) ? "Gemini" : "Groq";
                
                log.info("Switching to {} for failover request", fallbackName);
                try {
                    return fallbackModel.chat(chatRequest);
                } catch (Exception ex) {
                    log.error("Failover provider {} also failed: {}", fallbackName, ex.getMessage());
                    throw ex;
                }
            }
            throw e;
        }
    }

    private boolean isRetryableError(String msg) {
        if (msg == null) return false;
        String lowerMsg = msg.toLowerCase();
        
        // General HTTP and common AI provider error patterns
        return lowerMsg.contains("429") || 
               lowerMsg.contains("limit") || 
               lowerMsg.contains("quota") || 
               lowerMsg.contains("resource_exhausted") || // Specific to Google/Gemini
               lowerMsg.contains("exhausted") ||
               lowerMsg.contains("timeout") ||
               lowerMsg.contains("overloaded") ||
               lowerMsg.contains("500") ||
               lowerMsg.contains("502") ||
               lowerMsg.contains("503") ||
               lowerMsg.contains("504") ||
               lowerMsg.contains("unavailable") ||
               lowerMsg.contains("deadline_exceeded") || // Common in gRPC/Google APIs
               lowerMsg.contains("tool_use_failed") ||
               lowerMsg.contains("failed to call a function");
    }
}
