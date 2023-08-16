package com.example.weather.dto;

import com.example.weather.entity.Weather;
import com.example.weather.redis.entity.RedisWeather;

public record WeatherDTO(
        String cityName,
        String country,
        Integer temperature,
        Integer wind_speed,
        Integer pressure,
        Integer humidity
) {
    public static WeatherDTO convertToWeather(Weather from) {
        return new WeatherDTO(from.getCityName(), from.getCountry(),
                from.getTemperature(), from.getWind_speed(), from.getPressure(), from.getHumidity());
    }

    public static WeatherDTO convert(RedisWeather from) {
        return new WeatherDTO(from.getCityName(), from.getCountry(),
                from.getTemperature(), from.getWind_speed(),
                from.getPressure(), from.getHumidity());
    }
}
