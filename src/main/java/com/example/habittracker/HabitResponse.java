package com.example.habittracker;

import java.util.List;

// This is the JSON we send to the web page for each habit.
// We use a separate class (not Habit) so the page gets exactly what it needs:
// the streak and the last 7 days, already calculated.
public record HabitResponse(
        Long id,
        String name,
        int streak,
        boolean doneToday,
        List<DayStatus> week
) {
}
