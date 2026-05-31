package com.travelbuddy.tourism.service.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ToolAiAssistant {

    @SystemMessage(fromResource = "TravelGuidePrompt.txt")
    String chatByTravelGuide(@MemoryId int memoryId, @UserMessage String userMessage);
}
