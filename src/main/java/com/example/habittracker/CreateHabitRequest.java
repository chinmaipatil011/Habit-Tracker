package com.example.habittracker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// This describes the JSON the browser sends when adding a habit: {"name": "Gym"}
// A "record" is a short way to write a class that only holds data.
public record CreateHabitRequest(
        @NotBlank(message = "Give your habit a name.")
        @Size(max = 40, message = "Keep the name under 40 characters.")
        String name
) {
}
