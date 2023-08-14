package com.example.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class Weather {
    @Id
    @UuidGenerator
    String id;
    String requestedCityName;
    String cityName;
    String country;
    Integer temperature;
    LocalDateTime updatedTime;
    LocalDateTime responseLocalTime;

    Integer wind_speed;
    Integer pressure;
    Integer humidity;

    public Weather(String id, String requestedCityName, String cityName, String country, Integer temperature, LocalDateTime updatedTime, LocalDateTime responseLocalTime, Integer wind_speed, Integer pressure, Integer humidity) {
        this.id = id;
        this.requestedCityName = requestedCityName;
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.updatedTime = updatedTime;
        this.responseLocalTime = responseLocalTime;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Weather(String requestedCityName, String cityName, String country, Integer temperature, LocalDateTime updatedTime, LocalDateTime responseLocalTime, Integer wind_speed, Integer pressure, Integer humidity) {
        this.id = "";
        this.requestedCityName = requestedCityName;
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.updatedTime = updatedTime;
        this.responseLocalTime = responseLocalTime;
        this.wind_speed = wind_speed;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Weather() {
    }

    public String getId() {
        return id;
    }

    public String getRequestedCityName() {
        return requestedCityName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public LocalDateTime getResponseLocalTime() {
        return responseLocalTime;
    }
}
