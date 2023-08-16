package com.example.weather.redis.repository;

import com.example.weather.redis.entity.RedisWeather;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisWeatherRepository extends CrudRepository<RedisWeather, String> {
    Optional<RedisWeather> findFirstByCityName(String city);
}
