package com.example.habittracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication = "start Spring Boot here".
// Running the main method below starts the whole web server.
@SpringBootApplication
public class HabitTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitTrackerApplication.class, args);
    }
}
