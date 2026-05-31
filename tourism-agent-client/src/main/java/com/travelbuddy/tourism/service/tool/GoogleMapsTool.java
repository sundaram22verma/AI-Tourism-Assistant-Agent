package com.travelbuddy.tourism.service.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleMapsTool {

    @Value("${google.maps.apiKey}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Tool("Find famous tourist places, hotels and restaurants in any city worldwide")
    public String getTouristSpots(String city) {
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=top+tourist+places+in=" + city + "&key=" + apiKey;
        
        try {
            String rawJson = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(rawJson);
            JsonNode results = root.get("results");
            
            if (results == null || !results.isArray() || results.isEmpty()) {
                return "No tourist spots found for " + city;
            }

            List<String> simplifiedResults = new ArrayList<>();
            for (int i = 0; i < Math.min(results.size(), 10); i++) {
                JsonNode place = results.get(i);
                String name = place.path("name").asText();
                double rating = place.path("rating").asDouble();
                String address = place.path("formatted_address").asText();
                double lat = place.path("geometry").path("location").path("lat").asDouble();
                double lng = place.path("geometry").path("location").path("lng").asDouble();
                
                List<String> typesList = new ArrayList<>();
                place.path("types").forEach(type -> typesList.add(type.asText()));
                String types = String.join(", ", typesList);
                
                simplifiedResults.add(String.format("- %s (Rating: %.1f, Location: %.6f, %.6f): %s. Types: [%s]", 
                        name, rating, lat, lng, address, types));
            }
            
            return "Top tourist spots in " + city + ":\n" + String.join("\n", simplifiedResults);
        } catch (Exception e) {
            return "Error fetching or parsing tourist spots: " + e.getMessage();
        }
    }
}
