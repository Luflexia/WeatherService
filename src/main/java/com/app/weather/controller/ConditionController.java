package com.app.weather.controller;

import com.app.weather.dto.ConditionDTO;
import com.app.weather.model.Condition;
import com.app.weather.model.Weather;
import com.app.weather.repository.ConditionRepository;
import com.app.weather.repository.WeatherRepository;
import com.app.weather.service.ConditionService;
import com.app.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conditions")
public class ConditionController {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ConditionService conditionService;

    // Создание нового условия погоды для определенной записи о погоде
    @PostMapping("/{weatherId}")
    public ConditionDTO createCondition(@PathVariable Long weatherId, @RequestBody ConditionDTO conditionDTO) {
        Weather weather = weatherService.getWeatherById(weatherId);
        Condition newCondition = convertToEntity(conditionDTO);
        newCondition.setWeather(weather);

        Condition createdCondition = conditionService.createCondition(newCondition);
        return convertToDTO(createdCondition);
    }

    // Получение всех условий погоды для определенной записи о погоде
    @GetMapping("/{weatherId}")
    public ResponseEntity<List<Condition>> getAllConditions(@PathVariable Long weatherId) {
        return weatherRepository.findById(weatherId).map(weather -> ResponseEntity.ok().body(weather.getConditions()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Обновление информации о конкретном условии погоды
    @PutMapping("/{weatherId}")
    public ResponseEntity<?> updateCondition(@PathVariable Long weatherId, @RequestBody Condition conditionDTO) {
        Weather weather = weatherRepository.findById(weatherId).orElse(null);
        if (weather != null) {
            Condition condition = conditionRepository.findByWeather(weather);
                condition.setText(conditionDTO.getText());
                conditionRepository.save(condition);
                return ResponseEntity.ok().build();
            }
        return ResponseEntity.notFound().build();
    }

    // Удаление конкретного условия погоды
    @DeleteMapping("/{weatherId}")
    public ResponseEntity<?> deleteCondition(@PathVariable Long weatherId) {
        Weather weather = weatherRepository.findById(weatherId).orElse(null);
        if (weather != null) {
            Condition condition = conditionRepository.findByWeather(weather);
            weather.removeCondition(condition);
            weatherRepository.save(weather);
            conditionRepository.delete(condition);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ConditionDTO convertToDTO(Condition condition) {
        return new ConditionDTO(condition.getId(), condition.getText(), condition.getWeather().getId());
    }

    private Condition convertToEntity(ConditionDTO conditionDTO) {
        Condition condition = new Condition();
        condition.setId(conditionDTO.getId());
        condition.setText(conditionDTO.getText());
        return condition;
    }
}