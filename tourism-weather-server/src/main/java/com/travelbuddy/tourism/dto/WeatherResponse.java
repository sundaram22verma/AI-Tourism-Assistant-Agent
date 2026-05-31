package com.travelbuddy.tourism.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
public class WeatherResponse {

    @JsonProperty("obsTime")
    @JsonPropertyDescription("Data observation time")
    private String obsTime;

    @JsonProperty("temp")
    @JsonPropertyDescription("Temperature (Celsius)")
    private String temp;

    @JsonProperty("feelsLike")
    @JsonPropertyDescription("Apparent temperature (Celsius)")
    private String feelsLike;

    @JsonProperty("icon")
    @JsonPropertyDescription("Weather condition icon code")
    private String icon;

    @JsonProperty("text")
    @JsonPropertyDescription("Weather condition description")
    private String text;

    @JsonProperty("wind360")
    @JsonPropertyDescription("Wind direction in degrees (360)")
    private String wind360;

    @JsonProperty("windDir")
    @JsonPropertyDescription("Wind direction")
    private String windDir;

    @JsonProperty("windScale")
    @JsonPropertyDescription("Wind scale")
    private String windScale;

    @JsonProperty("windSpeed")
    @JsonPropertyDescription("Wind speed (km/h)")
    private String windSpeed;

    @JsonProperty("humidity")
    @JsonPropertyDescription("Relative humidity (%)")
    private String humidity;

    @JsonProperty("precip")
    @JsonPropertyDescription("Precipitation in the last hour (mm)")
    private String precip;

    @JsonProperty("pressure")
    @JsonPropertyDescription("Atmospheric pressure (hPa)")
    private String pressure;

    @JsonProperty("vis")
    @JsonPropertyDescription("Visibility (km)")
    private String vis;

    @JsonProperty("cloud")
    @JsonPropertyDescription("Cloud cover (%), may be null")
    private String cloud;

    @JsonProperty("dew")
    @JsonPropertyDescription("Dew point temperature, may be null")
    private String dew;
}
