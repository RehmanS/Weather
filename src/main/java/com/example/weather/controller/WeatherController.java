package com.example.weather.controller;

import com.example.weather.controller.validation.CityNameConstraint;
import com.example.weather.dto.WeatherDTO;
import com.example.weather.service.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/weather")
@Validated // String ucun validationlarin islemesi ucun
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    @RateLimiter(name = "basic") // application.properties-de yazdigimizin islemesi ucun(userin request limiti)
    public ResponseEntity<WeatherDTO> getWeather(@PathVariable("city") @NotBlank @CityNameConstraint String city) {
        return ResponseEntity.ok(weatherService.getWeatherByCityName(city));
    }
}
