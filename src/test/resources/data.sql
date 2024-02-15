INSERT INTO movies (movie_title, showing) VALUES ('Movie', TRUE);
INSERT INTO sessions (movie_id, session_date, session_time, sold_out) VALUES (1, '2023-01-01', '10:00', FALSE);
INSERT INTO users (first_name, last_name, email) VALUES ('ExistName', 'ExistLastName', 'exist@email.com');
INSERT INTO tickets (session_id, user_id, seat_id) VALUES (1, 1, 1);