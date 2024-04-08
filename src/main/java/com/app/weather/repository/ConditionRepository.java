package com.app.weather.repository;

import com.app.weather.model.Condition;
import com.app.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
    Condition findByWeather(Weather weather);
}