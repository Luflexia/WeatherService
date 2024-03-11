package com.Lab1.init;

import com.Lab1.dao.WeatherDAO;
import com.Lab1.model.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final WeatherDAO weatherDAO;

    @Autowired
    public DataInitializer(WeatherDAO weatherDAO) {
        this.weatherDAO = weatherDAO;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем и сохраняем данные о погоде для инициализации
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setLocation("YourLocation");
        weatherEntity.setTemperature("YourTemperature");
        // Установите другие значения по необходимости

        weatherDAO.save(weatherEntity);
    }
}
