package com.app.weather.service;

import com.app.weather.dto.ConditionDTO;
import com.app.weather.dto.WeatherDTO;
import com.app.weather.model.Condition;
import com.app.weather.model.Weather;
import com.app.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }
    public List<Weather> getAllWeather() {
        return weatherRepository.findAll();
    }

    public void deleteWeatherByCity(String city) {
        Weather weather = weatherRepository.findByCity(city);
        if (weather != null) {
            weatherRepository.delete(weather);
        }
    }

    public Weather getWeatherById(Long weatherId) {
        return weatherRepository.findById(weatherId).orElse(null);
    }

    public Weather updateWeather(String city, Weather updatedWeather) {
        Weather existingWeather = weatherRepository.findByCity(city);
        existingWeather.setTemperature(updatedWeather.getTemperature());
        existingWeather.setDate(Timestamp.valueOf(LocalDateTime.now()));
        return weatherRepository.save(existingWeather);
    }

    public Weather createWeather(String city, Weather weather) {
        Weather existingWeather = weatherRepository.findByCity(city);

        if (existingWeather != null) {
            // Если запись с таким городом уже существует, обновляем ее
            existingWeather.setTemperature(weather.getTemperature());
            existingWeather.setDate(new Timestamp(System.currentTimeMillis()));
            return weatherRepository.save(existingWeather);
        } else {
            // Если записи с таким городом нет, создаем новую
            weather.setCity(city);
            weather.setDate(new Timestamp(System.currentTimeMillis()));
            return weatherRepository.save(weather);
        }
    }
    public Weather getWeather(String city) {
        return weatherRepository.findByCity(city);
    }

    public WeatherDTO convertToDTO(Weather weather) {
        WeatherDTO dto = new WeatherDTO();
        dto.setId(weather.getId());
        dto.setCity(weather.getCity());
        dto.setDate(weather.getDate());
        dto.setTemperature(weather.getTemperature());

        List<ConditionDTO> conditionDTOs = weather.getConditions().stream()
                .map(this::convertConditionToDTO)
                .toList();
        dto.setConditions(conditionDTOs);
        return dto;
    }

    public ConditionDTO convertConditionToDTO(Condition condition) {
        return new ConditionDTO(condition.getId(), condition.getText(), condition.getWeatherId());
    }
    public Weather convertToEntity(WeatherDTO weatherDTO) {
        Weather weather = new Weather();
        weather.setId(weatherDTO.getId());
        weather.setCity(weatherDTO.getCity());
        weather.setTemperature(weatherDTO.getTemperature());
        return weather;
    }

}
