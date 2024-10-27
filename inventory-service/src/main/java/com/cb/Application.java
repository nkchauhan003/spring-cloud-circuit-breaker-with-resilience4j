package com.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the Spring Boot application.
 */
@SpringBootApplication
public class Application {

    /**
     * The main method that serves as the entry point for the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}