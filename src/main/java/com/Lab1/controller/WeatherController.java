// Ваш класс контроллера (WeatherController)
package com.Lab1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Lab1.model.WeatherEntity;  // Добавьте этот импорт
import com.Lab1.service.WeatherService;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherEntity> getWeatherByLocation(@RequestParam String location) {
        WeatherEntity weather = weatherService.getWeatherByLocation(location);
        return ResponseEntity.ok(weather);
    }
}
