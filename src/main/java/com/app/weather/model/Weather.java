package com.app.weather.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weather")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "city")
    private String city;

    @Column(name = "temperature")
    private double temperature;

    // Двунаправленная связь One-to-Many
    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Condition> conditions = new ArrayList<>();

    public void addCondition(Condition condition) {
        conditions.add(condition);
        condition.setWeather(this);
    }

    public void removeCondition(Condition condition) {
        conditions.remove(condition);
        condition.setWeather(null);
    }

}