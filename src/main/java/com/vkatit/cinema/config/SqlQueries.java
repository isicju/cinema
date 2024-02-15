package com.vkatit.cinema.config;

import org.springframework.stereotype.Component;

@Component
public class SqlQueries {

    public final String GET_SESSIONS_MOVIES_SEATS = """
                SELECT
                    sessions.session_id,
                    sessions.session_date,
                    sessions.session_time,
                    movies.movie_id,
                    movies.movie_title,
                    ARRAY_AGG(tickets.seat_id) AS occupied_seats
                FROM
                    sessions
                    JOIN movies ON sessions.movie_id = movies.movie_id
                    LEFT JOIN tickets ON sessions.session_id = tickets.session_id
                WHERE
                    movies.showing = TRUE
                    AND sessions.sold_out = FALSE
                GROUP BY
                    sessions.session_id, sessions.session_date, sessions.session_time, movies.movie_id, movies.movie_title;
            """;

    public final String INSERT_USER = """
                INSERT INTO users (first_name, last_name, email)
                VALUES (?, ?, ?)
                ON CONFLICT (email) DO UPDATE SET email = excluded.email
                RETURNING user_id;
            """;

    public final String INSERT_TICKET = """
                INSERT INTO tickets (session_id, seat_id, user_id)
                VALUES (?, ?, ?)
                RETURNING ticket_id;
            """;

    public final String GET_TICKET = """
                SELECT
                    m.movie_title,
                    u.first_name,
                    s.session_date,
                    s.session_time,
                    t.seat_id
                FROM
                    tickets t
                    JOIN sessions s ON t.session_id = s.session_id
                    JOIN movies m ON s.movie_id = m.movie_id
                    JOIN users u ON t.user_id = u.user_id
                WHERE
                    t.ticket_id = ?;
            """;

}