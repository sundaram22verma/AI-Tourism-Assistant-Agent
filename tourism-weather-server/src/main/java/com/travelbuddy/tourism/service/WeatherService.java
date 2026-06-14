package com.travelbuddy.tourism.service;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.travelbuddy.tourism.dto.WeatherRequest;
import com.travelbuddy.tourism.dto.WeatherResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j 
public class WeatherService {

    @Value("${hefeng.weather.host}")
    private String apiHost;

    @Value("${hefeng.api.key}")
    private String apiKey;

    @Tool(description = "Get real-time weather details for a target city")
    public WeatherResponse getWeather(WeatherRequest request) {
        String city = request.getCity();
        if (city == null || city.trim().isEmpty()) {
            log.warn("City name is empty, skipping weather lookup");
            return null;
        }
        if (city.contains(",")) {
            city = city.split(",")[0].trim();
        }
        log.info("Fetching target location weather context for: {}", city);

        try {
            String citySearchApiUrl = "https://geoapi.qweather.com/v2/city/lookup";
            if (apiHost != null && !apiHost.contains("devapi.qweather.com") && !apiHost.contains("api.qweather.com")) {
                citySearchApiUrl = apiHost + "/geo/v2/city/lookup";
            }

            @Cleanup
            HttpResponse citySearchHttpResponse = HttpUtil.createGet(citySearchApiUrl)
                    .header("X-QW-Api-Key", apiKey)
                    .form("location", city)
                    .timeout(5000)
                    .execute();

            log.debug("City lookup request URL: {}", citySearchHttpResponse.toString());
            String responseBody = citySearchHttpResponse.body();
            log.debug("City lookup response body: {}", responseBody);

            if (!citySearchHttpResponse.isOk()) {
                log.error("City coordinates lookup failed: {} | URL: {} | Response: {}", 
                    citySearchHttpResponse.getStatus(), citySearchApiUrl, responseBody);
                return null;
            }

            JSONObject cityJson = JSONUtil.parseObj(citySearchHttpResponse.body());
            if (!"200".equals(cityJson.getStr("code"))) {
                return null;
            }

            String locationId = JSONUtil.getByPath(cityJson, "$.location[0].id", null);
            if (locationId == null) {
                return null;
            }

            @Cleanup
            HttpResponse weatherHttpResponse = HttpUtil.createGet(apiHost + "/v7/weather/now")
                    .header("X-QW-Api-Key", apiKey)
                    .form("location", locationId)
                    .form("lang", "en")
                    .timeout(5000)
                    .execute();

            if (!weatherHttpResponse.isOk()) {
                return null;
            }

            JSONObject weatherJson = JSONUtil.parseObj(weatherHttpResponse.body());
            if (!"200".equals(weatherJson.getStr("code"))) {
                return null;
            }

            JSONObject now = weatherJson.getJSONObject("now");
            return now.toBean(WeatherResponse.class);

        } catch (Exception e) {
            log.error("Failed to compile target MCP weather metrics: {}", e.getMessage(), e);
            return null;
        }
    }
}
