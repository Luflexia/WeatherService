package com.app.weather.service;

import com.app.weather.component.CacheComponent;
import com.app.weather.dto.ConditionDTO;
import com.app.weather.dto.WeatherDTO;
import com.app.weather.model.Condition;
import com.app.weather.model.Weather;
import com.app.weather.repository.WeatherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final ConditionService conditionService;
    private final CacheComponent cacheComponent;

    public WeatherService(WeatherRepository weatherRepository, ConditionService conditionService, CacheComponent cacheComponent) {
        this.weatherRepository = weatherRepository;
        this.conditionService = conditionService;
        this.cacheComponent = cacheComponent;
    }
    @Transactional
    public Weather createWeatherWithCondition(WeatherDTO weatherDTO) {
        Weather weather = convertToEntity(weatherDTO);
        weather.setDate(new Timestamp(System.currentTimeMillis()));

        // Проверяем, существует ли уже погода для этого города
        String cacheKey = weather.getCity();
        Weather existingWeather = (Weather) cacheComponent.get(cacheKey);
        if (existingWeather != null) {
            return existingWeather;
        }
        Weather weatherFromDb = weatherRepository.findByCity(weather.getCity());
        if (weatherFromDb != null) {
            cacheComponent.put(cacheKey, weatherFromDb);
            return weatherFromDb;
        }
        // Проверяем, существует ли условие, если нет, то создаем его
        Condition condition = conditionService.getConditionByText(weatherDTO.getCondition().getText());
        if (condition == null) {
            condition = conditionService.convertToEntity(weatherDTO.getCondition());
            condition = conditionService.createCondition(condition); // сохраняем объект Condition в базе данных
        }

        // Устанавливаем связь между погодой и условием
        weather.setCondition(condition);
        condition.addWeather(weather);

        // Создаем новую погоду
        Weather savedWeather = weatherRepository.save(weather);
        cacheComponent.put(cacheKey, savedWeather);
        return savedWeather;
    }
    @Transactional
    public Weather updateWeather(Long id, WeatherDTO weatherDTO) {
        Weather existingWeather = weatherRepository.findById(id).orElse(null);
        if (existingWeather == null) {
            return null;
        }
        existingWeather.setDate(new Timestamp(System.currentTimeMillis()));
        existingWeather.setTemperature(weatherDTO.getTemperature());

        // Проверяем, существует ли уже погода для этого города
        Weather weatherByCity = weatherRepository.findByCity(weatherDTO.getCity());
        if (weatherByCity != null && !weatherByCity.getId().equals(id)) {
            return weatherByCity;
        }

        // Проверяем, существует ли условие, если нет, то создаем его
        Condition condition = conditionService.getConditionByText(weatherDTO.getCondition().getText());
        if (condition == null) {
            condition = conditionService.convertToEntity(weatherDTO.getCondition());
        }
        existingWeather.setCondition(condition);

        Weather savedWeather = weatherRepository.save(existingWeather);
        cacheComponent.put(savedWeather.getCity(), savedWeather);
        return savedWeather;
    }
    @Transactional
    public void deleteWeather(Long id) {
        Weather weather = weatherRepository.findById(id).orElse(null);
        if (weather == null) {
            return;
        }
        cacheComponent.remove(weather.getCity() + ":" + weather.getDate() + ":" + weather.getTemperature() + ":" + weather.getCondition().getText());
        weatherRepository.deleteById(id);
    }
    @Transactional
    public Weather getWeatherById(Long id) {
        Weather weather = weatherRepository.findById(id).orElse(null);
        if (weather == null) {
            return null;
        }
        cacheComponent.put(weather.getCity(), weather);
        return weather;
    }
    @Transactional
    public List<Weather> getAllWeathers() {
        List<Weather> weathers = weatherRepository.findAll();
        weathers.forEach(weather -> cacheComponent.put(weather.getCity(), weather));
        return weathers;
    }
    @Transactional
    public WeatherDTO convertToDTO(Weather weather) {
        WeatherDTO dto = new WeatherDTO();
        dto.setId(weather.getId());
        dto.setCity(weather.getCity());
        dto.setDate(weather.getDate());
        dto.setTemperature(weather.getTemperature());

        ConditionDTO conditionDTO = conditionService.convertToDTO(weather.getCondition());
        dto.setCondition(conditionDTO);
        return dto;
    }

    private Weather convertToEntity(WeatherDTO weatherDTO) {
        Weather weather = new Weather();
        weather.setCity(weatherDTO.getCity());
        weather.setDate(weatherDTO.getDate());
        weather.setTemperature(weatherDTO.getTemperature());
        // Создаем объект Condition на основе conditionText
        Condition condition = new Condition();
        condition.setText(weatherDTO.getCondition().getText());
        weather.setCondition(condition);
        return weather;
    }

    public List<WeatherDTO> findByTemperature(double temperature) {
        List<Weather> weathers = weatherRepository.findByTemperature(temperature);
        return weathers.stream()
                .map(this::convertToDTO)
                .toList();
    }
}