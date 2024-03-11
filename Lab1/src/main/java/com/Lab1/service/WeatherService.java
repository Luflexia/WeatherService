package com.Lab1.service;

import com.Lab1.dao.WeatherDAO;
import com.Lab1.model.WeatherEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherService {
    private final WeatherDAO weatherDAO;

    @Autowired
    public WeatherService(WeatherDAO weatherDAO) {
        this.weatherDAO = weatherDAO;
    }

    public WeatherEntity getWeatherByLocation(String location) {
        Optional<WeatherEntity> optionalWeather = weatherDAO.findByLocation(location);
        return optionalWeather.orElse(null);
    }
}
