package com.example.habittracker;

import org.springframework.data.jpa.repository.JpaRepository;

// A Repository is the class that talks to the database.
// We write NO code inside it. Spring builds methods like
// save(), findAll(), findById() and deleteById() for us.
// <Habit, Long> = "this repository handles Habit objects whose id is a Long".
public interface HabitRepository extends JpaRepository<Habit, Long> {
}
