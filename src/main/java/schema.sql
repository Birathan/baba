CREATE TABLE todo_list_test(
    id SERIAL PRIMARY KEY,
    task VARCHAR(45) NOT NULL,
    completed BOOLEAN NOT NULL
);

CREATE TABLE reminder(
    reminder VARCHAR(45) NOT NULL,
    reminder_time  TIME NOT NULL
);