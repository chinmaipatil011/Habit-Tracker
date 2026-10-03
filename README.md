# Habit Tracker

A small web app: add habits, tick off days, see your streak.
Built with Java, Spring Boot, and a plain HTML/CSS/JS page.
Data is saved in a file, so it survives restarts.

## Words you'll see

- **Spring Boot**: a Java framework that sets up a web server for you.
- **Maven**: a tool that downloads the libraries the project needs (listed in `pom.xml`).
- **Database (H2)**: where habits are stored. It lives in a `data/` folder that appears after the first run.
- **API**: the URLs the web page calls to get and change data (all start with `/api/habits`).

## What you need

1. **JDK 17 or newer** (the Java toolkit). Check in a terminal: `java -version`
2. **IntelliJ IDEA Community** (free). It has Maven built in, so you don't install Maven yourself.

## Run it (IntelliJ, easiest)

1. Unzip the project.
2. IntelliJ: **File > Open**, pick the `habit-tracker` folder (the one with `pom.xml`).
3. Wait until the bottom bar finishes loading (Maven is downloading libraries; first time takes a few minutes).
4. Open `src/main/java/com/example/habittracker/HabitTrackerApplication.java` and click the green run button next to `main`.
5. When the log says `Started HabitTrackerApplication`, open **http://localhost:8080**.

## Run it (terminal)

Only if you already have Maven installed (`mvn -v` works):

```
mvn spring-boot:run
```

## How to test it works

1. Add a habit. A pastel card appears.
2. Tap today's circle. It fills in and the streak becomes 1.
3. Tap yesterday's circle too. The streak becomes 2.
4. Stop the app and start it again. Your habits are still there.

## Folder map

```
pom.xml                                  libraries list
src/main/resources/
  application.properties                 database + server settings
  static/index.html, style.css, app.js   the web page (frontend)
src/main/java/com/example/habittracker/
  HabitTrackerApplication.java           starts the app
  Habit.java                             one habit = one database row
  HabitRepository.java                   talks to the database
  HabitService.java                      the logic: streaks, tick/untick
  HabitController.java                   the web URLs (API)
  CreateHabitRequest.java                what the page sends when adding
  HabitResponse.java, DayStatus.java     what the page receives
  ApiExceptionHandler.java               friendly error messages
```

## Change the colors

Open `static/style.css`. The colors are at the top (`:root`) and in the `.c0` to `.c5` lines.

## Common errors

- **Port 8080 already in use**: another app (like your old Task Manager) is still running. Stop it, or change `server.port` in `application.properties`.
- **`release version 17 not supported`**: your JDK is older than 17. Install JDK 17+.
- **Blank page**: check the app is running, and open `http://localhost:8080` (not a file from your folder).
- **Want to wipe all data**: stop the app and delete the `data/` folder.

## Known limitations

- No login: everyone who opens the page shares the same habits.
- "Today" uses the server's clock (fine when it runs on your own computer).
- Pastel page is basic on purpose.

## Next steps

1. Add a login so each person has their own habits (Spring Security).
2. Write tests for `HabitService` (especially the streak logic).
3. Switch H2 to PostgreSQL and deploy it online.
