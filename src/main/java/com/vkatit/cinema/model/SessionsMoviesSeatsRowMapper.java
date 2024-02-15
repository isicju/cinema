package com.vkatit.cinema.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class SessionsMoviesSeatsRowMapper implements RowMapper<SessionsMoviesSeats> {

    @Override
    public SessionsMoviesSeats mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array pgArray = rs.getArray("occupied_seats");
        SessionsMoviesSeats.SessionsMoviesSeatsBuilder sessionsMoviesSeats = SessionsMoviesSeats.builder()
                .movieId(rs.getInt("movie_id"))
                .sessionId(rs.getInt("session_id"))
                .sessionTime(rs.getTime("session_time").toLocalTime())
                .sessionDate(rs.getTimestamp("session_date").toLocalDateTime().toLocalDate())
                .movieTitle(rs.getString("movie_title"))
                .occupiedSeats(pgArray != null ? Arrays.asList((Integer[]) pgArray.getArray()) : Collections.emptyList());
        return sessionsMoviesSeats.build();
    }

}