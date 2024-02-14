package com.vkatit.cinema.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class SessionsMoviesSeatsRowMapper implements RowMapper<SessionsMoviesSeats> {

    @Override
    public SessionsMoviesSeats mapRow(ResultSet rs, int rowNum) throws SQLException {
        SessionsMoviesSeats sessionsMoviesSeats = new SessionsMoviesSeats();
        sessionsMoviesSeats.setMovieId(rs.getInt("movie_id"));
        sessionsMoviesSeats.setSessionId(rs.getInt("session_id"));
        sessionsMoviesSeats.setSessionTime(rs.getTime("session_time").toLocalTime());
        sessionsMoviesSeats.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime().toLocalDate());
        sessionsMoviesSeats.setMovieTitle(rs.getString("movie_title"));
        Array pgArray = rs.getArray("occupied_seats");
        sessionsMoviesSeats.setOccupiedSeats(pgArray != null ? Arrays.asList((Integer[]) pgArray.getArray()) : Collections.emptyList());
        return sessionsMoviesSeats;
    }

}