package com.Lab1.dao;

import com.Lab1.model.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherDAO extends JpaRepository<WeatherEntity, Long> {
    Optional<WeatherEntity> findByLocation(String location);
}
