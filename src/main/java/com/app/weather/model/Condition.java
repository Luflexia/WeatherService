package com.app.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "condition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    // Двунаправленная связь Many-to-One
    @ManyToOne
    @JoinColumn(name = "weatherId")
    @JsonIgnore
    private Weather weather;


    public Long getWeatherId() {
        return weather.getId();
    }
}