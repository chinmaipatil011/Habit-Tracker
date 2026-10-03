// This file runs in the browser. It talks to our Spring Boot server
// using fetch() and draws the habits on the page.

const API = "/api/habits";

const listEl = document.getElementById("list");
const formEl = document.getElementById("add-form");
const inputEl = document.getElementById("habit-name");
const messageEl = document.getElementById("message");

// Show today's date under the title.
document.getElementById("today-text").textContent =
  new Date().toLocaleDateString(undefined, { weekday: "long", day: "numeric", month: "long" });

// ---------- Helpers ----------

function showMessage(text) {
  messageEl.textContent = text;
  messageEl.hidden = false;
}

function clearMessage() {
  messageEl.hidden = true;
}

// Sends a request. If the server says "error", we throw it with a readable message.
async function request(url, options) {
  let response;
  try {
    response = await fetch(url, options);
  } catch (e) {
    throw new Error("Can't reach the server. Is the app running?");
  }

  if (!response.ok) {
    let text = "Something went wrong. Try again.";
    try {
      const body = await response.json();
      if (body.message) text = body.message;
    } catch (e) { /* the error had no JSON body, keep the default text */ }
    throw new Error(text);
  }

  if (response.status === 204) return null;   // 204 = success with nothing to send back
  return response.json();
}

// ---------- Drawing the page ----------

function renderHabits(habits) {
  listEl.innerHTML = "";

  if (habits.length === 0) {
    const empty = document.createElement("p");
    empty.className = "empty";
    empty.textContent = "No habits yet. Add your first one above.";
    listEl.appendChild(empty);
    return;
  }

  for (const habit of habits) {
    listEl.appendChild(buildCard(habit));
  }
}

function buildCard(habit) {
  const card = document.createElement("article");
  card.className = "card c" + (habit.id % 6);

  // Top row: name, streak text, delete button
  const top = document.createElement("div");
  top.className = "card-top";

  const info = document.createElement("div");
  const title = document.createElement("h2");
  title.textContent = habit.name;                 // textContent is safe against HTML injection
  const streak = document.createElement("p");
  streak.className = "streak";
  streak.textContent = habit.streak === 1 ? "🔥 1 day streak" : "🔥 " + habit.streak + " day streak";
  info.append(title, streak);

  const del = document.createElement("button");
  del.className = "delete";
  del.textContent = "Delete";
  del.setAttribute("aria-label", "Delete " + habit.name);
  del.onclick = () => removeHabit(habit);

  top.append(info, del);

  // The 7-day row of circles
  const week = document.createElement("div");
  week.className = "week";

  for (const day of habit.week) {
    const button = document.createElement("button");
    button.type = "button";
    button.className = "day" + (day.done ? " done" : "") + (day.today ? " today" : "");
    button.setAttribute("aria-pressed", String(day.done));
    button.setAttribute("aria-label", habit.name + ", " + day.date);

    const circle = document.createElement("span");
    circle.className = "circle";
    circle.textContent = day.done ? "✓" : "";

    const label = document.createElement("span");
    label.className = "label";
    label.textContent = day.today ? "Today" : day.label;

    button.append(circle, label);
    button.onclick = () => toggleDay(habit.id, day.date);
    week.appendChild(button);
  }

  card.append(top, week);
  return card;
}

// ---------- Talking to the server ----------

async function loadHabits() {
  try {
    renderHabits(await request(API));
  } catch (e) {
    showMessage(e.message);
  }
}

async function toggleDay(id, date) {
  try {
    await request(API + "/" + id + "/toggle?date=" + date, { method: "POST" });
    clearMessage();
    loadHabits();
  } catch (e) {
    showMessage(e.message);
  }
}

async function removeHabit(habit) {
  if (!confirm('Delete "' + habit.name + '"? This also deletes its history.')) return;
  try {
    await request(API + "/" + habit.id, { method: "DELETE" });
    clearMessage();
    loadHabits();
  } catch (e) {
    showMessage(e.message);
  }
}

formEl.addEventListener("submit", async (event) => {
  event.preventDefault();                          // stop the page from reloading
  try {
    await request(API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: inputEl.value })
    });
    inputEl.value = "";
    clearMessage();
    loadHabits();
  } catch (e) {
    showMessage(e.message);
  }
});

loadHabits();   // run once when the page opens
