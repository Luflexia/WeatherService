package com.app.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private Long id;
    private String city;
    private Timestamp date;
    private double temperature;
    private List<ConditionDTO> conditions;


    public WeatherDTO(Long id, String city, Timestamp date, double temperature) {
    }
}