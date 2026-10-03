package com.example.habittracker;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// The Service holds the "thinking" (business logic): streaks, toggling, rules.
// The Controller only receives requests; the Repository only talks to the database.
@Service
public class HabitService {

    private final HabitRepository repo;

    // Spring gives us the repository automatically ("dependency injection").
    public HabitService(HabitRepository repo) {
        this.repo = repo;
    }

    public List<HabitResponse> getAll() {
        return repo.findAll(Sort.by("id")).stream()
                .map(this::toResponse)
                .toList();
    }

    public HabitResponse create(String name) {
        Habit saved = repo.save(new Habit(name.trim()));
        return toResponse(saved);
    }

    // Tick a day if it's not ticked, untick it if it is.
    // @Transactional = "do all the database work in this method as one unit".
    @Transactional
    public HabitResponse toggle(Long id, LocalDate date) {
        Habit habit = findOrThrow(id);
        LocalDate day = (date == null) ? LocalDate.now() : date;

        if (day.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't tick a day in the future.");
        }

        if (habit.getCompletedDates().contains(day)) {
            habit.getCompletedDates().remove(day);
        } else {
            habit.getCompletedDates().add(day);
        }
        return toResponse(repo.save(habit));
    }

    public void delete(Long id) {
        findOrThrow(id);        // gives a 404 if the habit doesn't exist
        repo.deleteById(id);
    }

    private Habit findOrThrow(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "That habit doesn't exist."));
    }

    // Turn a Habit (database object) into a HabitResponse (what the page needs).
    private HabitResponse toResponse(Habit habit) {
        LocalDate today = LocalDate.now();
        Set<LocalDate> done = habit.getCompletedDates();

        // Last 7 days, oldest first, ending with today.
        List<DayStatus> week = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            week.add(new DayStatus(
                    day.toString(),
                    day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    done.contains(day),
                    i == 0));
        }

        return new HabitResponse(
                habit.getId(),
                habit.getName(),
                calculateStreak(done),
                done.contains(today),
                week);
    }

    // Streak = how many days in a row, counting back from today.
    // If today isn't ticked yet, the streak is still alive from yesterday.
    private int calculateStreak(Set<LocalDate> done) {
        LocalDate day = LocalDate.now();
        if (!done.contains(day)) {
            day = day.minusDays(1);
        }
        int streak = 0;
        while (done.contains(day)) {
            streak++;
            day = day.minusDays(1);
        }
        return streak;
    }
}
