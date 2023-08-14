package com.example.weather.service;

import com.example.weather.dto.WeatherDTO;
import com.example.weather.dto.WeatherResponse;
import com.example.weather.entity.Weather;
import com.example.weather.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"weathers"})
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather-stack.api-fullUrl}")
    private String URL;


    public WeatherService(WeatherRepository weatherRepository,
                          RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    @Cacheable(key = "#city")
    public WeatherDTO getWeatherByCityName(String city) {
        weatherRepository.deleteByUpdatedTimeIsBefore(LocalDateTime.now().minusMinutes(30));

        Optional<Weather> weatherOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTime(city);
        return weatherOptional.map(weather -> {
            if (weather.getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))) {
                return WeatherDTO.convert(getWeatherFromStack(city));
            }
            return WeatherDTO.convert(weather);
        }).orElseGet(() -> WeatherDTO.convert(getWeatherFromStack(city)));
    }

    private Weather getWeatherFromStack(String city) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                URL + city, String.class);
        try {
            WeatherResponse weatherResponse = objectMapper.readValue(responseEntity.getBody(), WeatherResponse.class);
            return saveWeatherEntity(city, weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @CacheEvict(allEntries = true)
    @PostConstruct
    @Scheduled(fixedRateString = "10000")
    public void clearCache() {
    }

    private Weather saveWeatherEntity(String city, WeatherResponse weatherResponse) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Weather weather = new Weather(
                city,
                weatherResponse.location().name(),
                weatherResponse.location().country(),
                weatherResponse.current().temperature(),
                LocalDateTime.now(),
                LocalDateTime.parse(weatherResponse.location().localtime(), dateTimeFormatter),
                weatherResponse.current().windSpeed(),
                weatherResponse.current().pressure(),
                weatherResponse.current().humidity());
        return weatherRepository.save(weather);
    }
}
