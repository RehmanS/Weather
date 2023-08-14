package com.example.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Location(
        String name,
        String country,
        String region,
        Double lat,
        Double lon,
        @JsonProperty("timezone_id")
        String timezoneid,
        String localtime,
        @JsonProperty("localtime_epoch")
        String localtimeEpoch,
        @JsonProperty("utc_offset")
        String utcOffset

) {
}
