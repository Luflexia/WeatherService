package com.app.weather.controller;

import com.app.weather.dto.WeatherDTO;
import com.app.weather.model.Weather;
import com.app.weather.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public WeatherDTO getWeather(@PathVariable String city) {
        Weather weather = weatherService.getWeather(city);
        return weatherService.convertToDTO(weather);
    }

    @PostMapping("/{city}")
    public WeatherDTO addWeather(@PathVariable String city, @RequestBody WeatherDTO weatherDTO) {
        Weather weather = weatherService.convertToEntity(weatherDTO);
        Weather createdWeather = weatherService.createWeather(city, weather);
        return weatherService.convertToDTO(createdWeather);
    }

    @GetMapping("/all")
    public List<WeatherDTO> getAllWeather() {
        List<Weather> allWeather = weatherService.getAllWeather();
        return allWeather.stream()
                .map(weatherService::convertToDTO)
                .toList();
    }

    @DeleteMapping("/{city}")
    public void deleteWeatherByCity(@PathVariable String city) {
        weatherService.deleteWeatherByCity(city);
    }

    @PutMapping("/{city}")
    public WeatherDTO updateWeather(@PathVariable String city, @RequestBody WeatherDTO weatherDTO) {
        Weather weather = weatherService.convertToEntity(weatherDTO);
        Weather updatedWeather = weatherService.updateWeather(city, weather);
        return weatherService.convertToDTO(updatedWeather);
    }
}