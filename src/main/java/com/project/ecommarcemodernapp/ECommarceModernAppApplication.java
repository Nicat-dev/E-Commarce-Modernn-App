package com.project.ecommarcemodernapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application entry point for E-Commerce Modern App.
 * Configures component scanning for entities and JPA repositories.
 */
@SpringBootApplication
public class ECommarceModernAppApplication {

    static void main(String[] args) {
        SpringApplication.run(ECommarceModernAppApplication.class, args);
    }

}
