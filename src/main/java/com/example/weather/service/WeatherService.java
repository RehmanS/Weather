package com.example.weather.service;

import com.example.weather.dto.WeatherDTO;
import com.example.weather.dto.WeatherResponse;
import com.example.weather.entity.Weather;
import com.example.weather.redis.entity.RedisWeather;
import com.example.weather.redis.repository.RedisWeatherRepository;
import com.example.weather.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final RedisWeatherRepository redisWeatherRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather-stack.api-fullUrl}")
    private String URL;

    // TODO : Updated time = api-den city-e uygun qayidan en son data

    public WeatherService(WeatherRepository weatherRepository,
                          RestTemplate restTemplate, RedisWeatherRepository redisWeatherRepository) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
        this.redisWeatherRepository = redisWeatherRepository;
    }

    @Transactional
    public WeatherDTO getWeatherByCityName(String city) {
        weatherRepository.deleteByUpdatedTimeIsBefore(LocalDateTime.now().minusMinutes(30));
        Optional<Weather> weatherOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTime(city);
        Optional<WeatherDTO> weatherDTO = readFromCache(city);
        if (!weatherDTO.isPresent()) {
            return weatherOptional.map(weather -> {
                if (weather.getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))) {
                    return WeatherDTO.convertToWeather(getWeatherFromStack(city));
                }
                return WeatherDTO.convertToWeather(weather);
            }).orElseGet(() -> WeatherDTO.convertToWeather(getWeatherFromStack(city)));
        }
        return weatherDTO.get();
    }

    private Optional<WeatherDTO> readFromCache(String city) {
        List<RedisWeather> redisList = (List<RedisWeather>) redisWeatherRepository.findAll();

        logger.info("---list---: "+redisList.size());
        if (!redisList.isEmpty()){
            Optional<WeatherDTO> weatherDTO = redisList
                    .stream()
                    .filter(redis -> redis != null && redis.getCityName() != null && redis.getCityName().contains(city))
                    .map(redis -> {
                        return WeatherDTO.convert(redis);
                    }).limit(1).findFirst();

            if (weatherDTO.isPresent())
                return weatherDTO;
            return Optional.empty();
        }
        return Optional.empty();
    }

    private Weather getWeatherFromStack(String city) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                URL + city, String.class);
        try {
            WeatherResponse weatherResponse = objectMapper.readValue(responseEntity.getBody(), WeatherResponse.class);
            saveToRedis(city, weatherResponse);
            return saveWeatherEntityToDB(city, weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private Weather saveWeatherEntityToDB(String city, WeatherResponse weatherResponse) {
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

    @Transactional
    private void saveToRedis(String city, WeatherResponse weatherResponse) {
        RedisWeather redisWeather = RedisWeather.builder()
                .cityName(city)
                .country(weatherResponse.location().country())
                .temperature(weatherResponse.current().temperature())
                .wind_speed(weatherResponse.current().windSpeed())
                .pressure(weatherResponse.current().pressure())
                .humidity(weatherResponse.current().humidity())
                .build();

        redisWeatherRepository.save(redisWeather);
    }
}
