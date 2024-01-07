CREATE SCHEMA IF NOT EXISTS cinema_schema;
SET search_path TO cinema_schema;

CREATE TABLE IF NOT EXISTS movies (
                                      movie_id SERIAL PRIMARY KEY,
                                      movie_title VARCHAR(255) NOT NULL,
    showing BOOLEAN
    );

CREATE TABLE IF NOT EXISTS sessions (
                                        session_id SERIAL PRIMARY KEY,
                                        movie_id INT REFERENCES movies(movie_id),
    session_date DATE,
    session_time TIME,
    sold_out BOOLEAN
    );

CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE
    );

CREATE TABLE IF NOT EXISTS tickets (
                                       ticket_id SERIAL PRIMARY KEY,
                                       session_id INT REFERENCES sessions(session_id),
    user_id INT REFERENCES users(user_id),
    seat_id INT
    );