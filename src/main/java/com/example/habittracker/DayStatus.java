package com.example.habittracker;

// One day in the 7-day row shown on the page.
// date  = "2026-10-03" (the format the page sends back when you click it)
// label = "Sat" (shown under the circle)
public record DayStatus(String date, String label, boolean done, boolean today) {
}
