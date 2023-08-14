package com.example.weather.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;



@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record WeatherResponse(
        Current current,
        Location location,
        Request request
) {
}
