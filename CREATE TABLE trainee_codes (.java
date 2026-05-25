CREATE TABLE trainee_codes (
  id SERIAL PRIMARY KEY,
  admin_id INT,
  code VARCHAR(10) UNIQUE
);

CREATE TABLE trainees (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE
);

CREATE TABLE students (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE
);

CREATE TABLE sessions (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE attendance (
  id SERIAL PRIMARY KEY,
  student_id INT REFERENCES students(id),
  session_id INT REFERENCES sessions(id)
);