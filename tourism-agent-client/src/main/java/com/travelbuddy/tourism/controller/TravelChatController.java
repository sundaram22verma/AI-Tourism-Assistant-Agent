package com.travelbuddy.tourism.controller;

import com.travelbuddy.tourism.service.TravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/v1/travel")
@RequiredArgsConstructor 
@Slf4j
@CrossOrigin(origins = "*")
public class TravelChatController {

    private final TravelService travelService;

    @GetMapping("/chat")
    public String travelChat(@RequestParam String content) {
        log.debug("Processing travel chat request: {}", content);
        return travelService.getTravelGuide(content);
    }
}
