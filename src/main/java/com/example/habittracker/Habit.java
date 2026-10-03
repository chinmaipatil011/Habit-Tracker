package com.example.habittracker;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// @Entity = "save this class as a table in the database".
// One Habit object = one row in the table.
@Entity
public class Habit {

    @Id                                                   // this field is the unique id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // the database picks the id (1, 2, 3...)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Every date this habit was completed. JPA stores these in a second table.
    // A Set never holds the same date twice, which is what we want.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "habit_completed_dates", joinColumns = @JoinColumn(name = "habit_id"))
    @Column(name = "completed_date")
    private Set<LocalDate> completedDates = new HashSet<>();

    // JPA needs an empty constructor.
    public Habit() {}

    public Habit(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<LocalDate> getCompletedDates() { return completedDates; }
}
