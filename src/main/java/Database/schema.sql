
CREATE TABLE IF NOT EXISTS todolist(
    id SERIAL PRIMARY KEY,
    task VARCHAR(45) NOT NULL,
    completed BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS reminders(
    reminder VARCHAR(45) NOT NULL,
    reminder_time  TIME NOT NULL
);