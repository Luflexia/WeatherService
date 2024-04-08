package com.app.weather.service;

import com.app.weather.dto.ConditionDTO;
import com.app.weather.model.Condition;
import com.app.weather.model.Weather;
import com.app.weather.repository.ConditionRepository;
import com.app.weather.repository.WeatherRepository;
import org.springframework.stereotype.Service;

@Service
public class ConditionService {
    private final ConditionRepository conditionRepository;
    private final WeatherRepository weatherRepository;

    public ConditionService(ConditionRepository conditionRepository, WeatherRepository weatherRepository) {
        this.conditionRepository = conditionRepository;
        this.weatherRepository = weatherRepository;
    }

    public Condition createCondition(Condition conditionDTO) {
        Weather weather = weatherRepository.findById(conditionDTO.getWeatherId()).orElse(null);

        if (weather != null) {
            Condition existingCondition = conditionRepository.findByWeather(weather);

            if (existingCondition != null) {
                // Запись уже существует, выполняем обновление поля "text"
                existingCondition.setText(conditionDTO.getText());
                return conditionRepository.save(existingCondition);
            } else {
                // Записи не существует, создаем новую запись
                Condition newCondition = new Condition();
                newCondition.setText(conditionDTO.getText());
                newCondition.setWeather(weather);
                return conditionRepository.save(newCondition);
            }
        }
        // Если запись погоды не найдена
        return null;
    }
    public Condition updateCondition(Long conditionId, ConditionDTO updatedConditionDTO) {
        return conditionRepository.findById(conditionId).map(condition -> {
            Weather weather = weatherRepository.findById(updatedConditionDTO.getWeatherId()).orElse(null);
            condition.setText(updatedConditionDTO.getText());
            condition.setWeather(weather);
            return conditionRepository.save(condition);

        }).orElse(null);
    }

    public boolean deleteCondition(Long conditionId) {
        return conditionRepository.findById(conditionId).map(condition -> {
            conditionRepository.delete(condition);
            return true;

        }).orElse(false);
    }
}