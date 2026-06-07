package com.travelbuddy.tourism.config;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class McpConfig {

    @org.springframework.beans.factory.annotation.Value("${mcp.weather.sse-url:http://localhost:9000/sse}")
    private String weatherSseUrl;

    @Bean
    public McpClient mcpClientWeather() {
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl(weatherSseUrl)
                .timeout(Duration.ofMinutes(5))
                .logRequests(true)
                .logResponses(true)
                .build();
        return new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
    }

    @Bean
    public McpToolProvider mcpToolProvider(List<McpClient> clients) {
        return McpToolProvider.builder()
                .mcpClients(clients)
                .build();
    }
}
