package com.app.weather.service;

import com.app.weather.component.CacheComponent;
import com.app.weather.component.CustomLogger;
import com.app.weather.dto.ConditionDTO;
import com.app.weather.exceptions.BadRequestException;
import com.app.weather.exceptions.InternalServerErrorException;
import com.app.weather.model.Condition;
import com.app.weather.repository.ConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConditionService {

    private static final String notFoundMsg = "Condition not found";
    private final ConditionRepository conditionRepository;
    private final CacheComponent cache;
    private final CustomLogger customLogger;
    private String cacheKey;
    private final ApplicationContext applicationContext;

    @Autowired
    public ConditionService(ConditionRepository conditionRepository, CacheComponent cache, CustomLogger customLogger, ApplicationContext applicationContext) {
        this.conditionRepository = conditionRepository;
        this.cache = cache;
        this.customLogger = customLogger;
        this.applicationContext = applicationContext;
    }

    @Transactional
    public Condition createCondition(Condition condition) {
        customLogger.info("Creating condition");
        if (conditionRepository.existsByText(condition.getText())) {
            throw new BadRequestException("Condition with this text already exists");
        }
        Condition savedCondition = conditionRepository.save(condition);
        cacheKey = savedCondition.getId().toString();
        cache.put(cacheKey, savedCondition);
        return savedCondition;
    }

    @Transactional
    public Condition updateCondition(Long id, ConditionDTO conditionDTO) {
        customLogger.info("Updating condition with id: " + id);
        Condition existingCondition = getConditionService().getConditionById(id);
        if (existingCondition == null) {
            throw new BadRequestException(notFoundMsg);
        }
        if (conditionRepository.existsByTextAndIdNot(conditionDTO.getText(), id)) {
            throw new BadRequestException("Condition with this text already exists");
        }
        existingCondition.setText(conditionDTO.getText());
        Condition savedCondition = conditionRepository.save(existingCondition);
        cacheKey = savedCondition.getId().toString();
        cache.put(cacheKey, savedCondition);
        return savedCondition;
    }

    @Transactional
    public boolean deleteCondition(Long id) {
        customLogger.info("Deleting condition with id: " + id);
        if (!conditionRepository.existsById(id)) {
            throw new BadRequestException(notFoundMsg);
        }
        conditionRepository.deleteById(id);
        cacheKey = id.toString();
        cache.remove(cacheKey);
        return true;
    }

    @Transactional
    public Condition getConditionById(Long id) {
        customLogger.info("Getting condition by id: {}" + id);
        try {
            cacheKey = id.toString();
            Condition condition = (Condition) cache.get(cacheKey);
            if (condition != null) {
                return condition;
            }
            condition = conditionRepository.findById(id).orElse(null);
            if (condition != null) {
                cache.put(cacheKey, condition);
            } else {
                throw new BadRequestException(notFoundMsg);
            }
            return condition;
        } catch (Exception e) {
            throw new InternalServerErrorException("Condition with this ID doesnt exists");
        }
    }

    @Transactional
    public List<Condition> getAllConditions() {
        customLogger.info("Getting all conditions");
        try {
            List<Condition> conditions = conditionRepository.findAll();
            conditions.forEach(condition -> {
                cacheKey = condition.getId().toString();
                cache.put(cacheKey, condition);
            });
            return conditions;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get all conditions");
        }
    }

    public Condition convertToEntity(ConditionDTO conditionDTO) {
        customLogger.info("Converting ConditionDTO to Condition");
        Condition condition = new Condition();
        condition.setText(conditionDTO.getText());
        return condition;
    }

    public ConditionDTO convertToDTO(Condition condition) {
        customLogger.info("Converting Condition to ConditionDTO");
        if (condition == null) {
            return null;
        }
        return new ConditionDTO(condition.getId(), condition.getText());
    }

    @Transactional

    public Condition getConditionByText(String text) {
        customLogger.info("Getting condition by text: " + text);
        try {
            Condition condition = conditionRepository.findByText(text);
            if (condition != null) {
                cacheKey = condition.getId().toString();
                cache.put(cacheKey, condition);
            }
            return condition;
        } catch (Exception e) {
            throw new InternalServerErrorException("Condition with this TEXT doesnt exists");
        }
    }

    private ConditionService getConditionService() {
        return applicationContext.getBean(ConditionService.class);
    }
}