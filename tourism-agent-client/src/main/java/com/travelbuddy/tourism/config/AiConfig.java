package com.travelbuddy.tourism.config;

import com.travelbuddy.tourism.model.FailoverChatModel;
import com.travelbuddy.tourism.service.AiProviderManager;
import com.travelbuddy.tourism.service.assistant.NormalAiAssistant;
import com.travelbuddy.tourism.service.assistant.ToolAiAssistant;
import com.travelbuddy.tourism.service.tool.GoogleMapsTool;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration 
@Slf4j
public class AiConfig {

    @Value("${llm.apiKey:}")
    private String apiKey;

    @Value("${llm.modelName:}")
    private String modelName;

    @Value("${groq.apiKey:}")
    private String groqApiKey;

    @Value("${groq.modelName:}")
    private String groqModelName;

    @Bean
    public ChatModel groqChatModel() {
        log.info("Initializing Groq ChatModel with model: {}", 
                groqModelName.isEmpty() ? "llama-3.3-70b-versatile" : groqModelName);
        return OpenAiChatModel.builder()
                .apiKey(groqApiKey)
                .modelName(groqModelName.isEmpty() ? "llama-3.3-70b-versatile" : groqModelName)
                .baseUrl("https://api.groq.com/openai/v1")
                .maxRetries(0) 
                .timeout(Duration.ofSeconds(180))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ChatModel geminiChatModel() {
        String effectiveModelName = modelName.isEmpty() ? "gemini-3.5-flash" : modelName;
        log.info("Initializing Gemini ChatModel with model: {}", effectiveModelName);
        
        return dev.langchain4j.model.googleai.GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(effectiveModelName)
                .maxRetries(0)
                .timeout(Duration.ofSeconds(180))
                .logRequestsAndResponses(true)
                .build();
    }

    @Bean
    public ChatModel failoverChatModel(AiProviderManager providerManager) {
        return new FailoverChatModel(providerManager);
    }

    @Bean
    public ToolAiAssistant aiAssistant(McpToolProvider mcpToolProvider, GoogleMapsTool googleMapsTool, ChatModel failoverChatModel) {
        return AiServices.builder(ToolAiAssistant.class)
                .chatModel(failoverChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .toolProvider(mcpToolProvider)
                .tools(googleMapsTool)
                .build();
    }

    @Bean
    public NormalAiAssistant normalAiAssistant(ChatModel failoverChatModel) {
        return AiServices.builder(NormalAiAssistant.class)
                .chatModel(failoverChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
