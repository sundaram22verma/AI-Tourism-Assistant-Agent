package com.travelbuddy.tourism.service;

import com.travelbuddy.tourism.service.assistant.NormalAiAssistant;
import com.travelbuddy.tourism.service.assistant.ToolAiAssistant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service 
@RequiredArgsConstructor 
@Slf4j
public class TravelService {

    private final ToolAiAssistant toolAiAssistant;
    private final NormalAiAssistant normalAiAssistant;

    public String getTravelGuide(String require) {
        log.info("Generating travel guide insights...");
        String guideResponse = toolAiAssistant.chatByTravelGuide(1, require);

        log.info("Starting HTML transformation step...");
        String rawHtml = normalAiAssistant.chatByTravelHtmlCreator(2, guideResponse);

        log.info("Double checking structural constraints on generated layouts...");
        String refinedHtml = normalAiAssistant.chatByDoubleCheck(3, rawHtml);

        return clearHtmlCode(refinedHtml);
    }

    private String clearHtmlCode(String htmlCode) {
        if (htmlCode == null || htmlCode.isEmpty()) {
            return "";
        }
        
        String result = htmlCode.trim();
        if (result.contains("```html")) {
            int start = result.indexOf("```html") + 7;
            int end = result.lastIndexOf("```");
            result = (end > start) ? result.substring(start, end) : result.substring(start);
        } else if (result.contains("```")) {
            int start = result.indexOf("```") + 3;
            int end = result.lastIndexOf("```");
            result = (end > start) ? result.substring(start, end) : result.substring(start);
        }
        
        int htmlStart = result.toLowerCase().indexOf("<!doctype");
        if (htmlStart == -1) {
            htmlStart = result.toLowerCase().indexOf("<html");
        }
        
        if (htmlStart != -1) {
            result = result.substring(htmlStart);
        }
        
        return result.trim();
    }
}
