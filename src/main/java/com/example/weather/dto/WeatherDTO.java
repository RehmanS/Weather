package com.example.weather.dto;

import com.example.weather.entity.Weather;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

public record WeatherDTO(
        String cityName,
        String country,
        Integer temperature,
        Integer wind_speed,
        Integer pressure,
        Integer humidity
) {
    public static WeatherDTO convert(Weather from) {
        return new WeatherDTO(from.getCityName(), from.getCountry(),
                from.getTemperature(), from.getWind_speed(), from.getPressure(), from.getHumidity());
    }
}
