package com.example.habittracker;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// The Controller receives web requests and passes them to the Service.
@RestController                  // "this class answers web requests with JSON"
@RequestMapping("/api/habits")   // every URL here starts with /api/habits
public class HabitController {

    private final HabitService service;

    public HabitController(HabitService service) {
        this.service = service;
    }

    // GET /api/habits  -> list all habits
    @GetMapping
    public List<HabitResponse> getAll() {
        return service.getAll();
    }

    // POST /api/habits  -> add a habit. @Valid runs the checks from CreateHabitRequest.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitResponse create(@Valid @RequestBody CreateHabitRequest request) {
        return service.create(request.name());
    }

    // POST /api/habits/1/toggle?date=2026-10-03  -> tick or untick that day
    // If no date is given, it uses today.
    @PostMapping("/{id}/toggle")
    public HabitResponse toggle(@PathVariable Long id,
                                @RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.toggle(id, date);
    }

    // DELETE /api/habits/1  -> remove a habit
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
