package com.Lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.Lab1.dao") // Указываем пакет для репозиториев
@EntityScan(basePackages = "com.Lab1.model") // Указываем пакет для сущностей
@ComponentScan(basePackages = {"com.Lab1", "com.Lab1.init"}) // Указываем пакеты для сканирования компонентов
public class Lab1Application {
	public static void main(String[] args) {
		SpringApplication.run(Lab1Application.class, args);
	}
}
