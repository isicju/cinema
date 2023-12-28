package com.vkatit.cinema.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class SessionsMoviesSeatsRowMapper implements RowMapper<SessionsMoviesSeatsDTO> {

    @Override
    public SessionsMoviesSeatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SessionsMoviesSeatsDTO dto = new SessionsMoviesSeatsDTO();
        dto.setMovieId(rs.getInt("movie_id"));
        dto.setSessionId(rs.getInt("session_id"));
        dto.setSessionTime(rs.getTime("session_time").toLocalTime());
        dto.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime().toLocalDate());
        dto.setMovieTitle(rs.getString("movie_title"));
        Array pgArray = rs.getArray("occupied_seats");
        dto.setOccupiedSeats(pgArray != null ? Arrays.asList((Integer[]) pgArray.getArray()) : Collections.emptyList());
        return dto;
    }

}