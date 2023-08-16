package com.example.weather.redis.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
@RedisHash(value = "RedisWeather", timeToLive = 1800L)
@Data
@Builder
public class RedisWeather implements Serializable {
    @Id
    @UuidGenerator
    String id;

    String cityName;
    String country;
    Integer temperature;
    Integer wind_speed;
    Integer pressure;
    Integer humidity;
}
