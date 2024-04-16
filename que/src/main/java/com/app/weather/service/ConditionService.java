package com.app.weather.service;

import com.app.weather.component.CacheComponent;
import com.app.weather.dto.ConditionDTO;
import com.app.weather.model.Condition;
import com.app.weather.repository.ConditionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {

    private final ConditionRepository conditionRepository;
    private final CacheComponent cacheComponent;

    public ConditionService(ConditionRepository conditionRepository, CacheComponent cacheComponent) {
        this.conditionRepository = conditionRepository;
        this.cacheComponent = cacheComponent;
    }
    @Transactional
    public Condition createCondition(Condition condition) {
        Condition existingCondition = conditionRepository.findByText(condition.getText());
        if (existingCondition != null) {
            return existingCondition;
        }
        return conditionRepository.save(condition);
    }
    @Transactional
    public Condition updateCondition(Long id, ConditionDTO conditionDTO) {
        Condition existingCondition = getConditionById(id);
        if (existingCondition == null) {
            return null;
        }
        Condition condition = conditionRepository.findByText(conditionDTO.getText());
        if (condition != null && !condition.getId().equals(id)) {
            return condition;
        }
        existingCondition.setText(conditionDTO.getText());
        return conditionRepository.save(existingCondition);
    }
    @Transactional
    public boolean deleteCondition(Long id) {
        if (!conditionRepository.existsById(id)) {
            return false;
        }
        conditionRepository.deleteById(id);
        return true;
    }
    @Transactional
    public Condition getConditionById(Long id) {
        Condition condition = (Condition) cacheComponent.get("condition:" + id);
        if (condition != null) {
            return condition;
        }
        condition = conditionRepository.findById(id).orElse(null);
        if (condition != null) {
            cacheComponent.put("condition:" + id, condition);
        }
        return condition;
    }
    @Transactional
    public List<Condition> getAllConditions() {
        return conditionRepository.findAll();
    }

    public Condition convertToEntity(ConditionDTO conditionDTO) {
        Condition condition = new Condition();
        condition.setText(conditionDTO.getText());
        return condition;
    }

    public ConditionDTO convertToDTO(Condition condition) {
        if (condition == null) {
            return null;
        }
        return new ConditionDTO(condition.getId(), condition.getText());
    }
    @Transactional
    public Condition getConditionByText(String text) {
        Condition condition = (Condition) cacheComponent.get("condition:" + text);
        if (condition != null) {
            return condition;
        }
        condition = conditionRepository.findByText(text);
        if (condition != null) {
            cacheComponent.put("condition:" + text, condition);
        }
        return condition;
    }
}