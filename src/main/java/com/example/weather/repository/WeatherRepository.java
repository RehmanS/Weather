package com.example.weather.repository;

import com.example.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, String> {
    Optional<Weather> findFirstByRequestedCityNameOrderByUpdatedTime(String city);
    void deleteByUpdatedTimeIsBefore(LocalDateTime thresholdTime);


}
