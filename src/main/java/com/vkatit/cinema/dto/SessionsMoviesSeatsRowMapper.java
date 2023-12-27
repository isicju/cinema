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
        dto.setMovie_id(rs.getInt("movie_id"));
        dto.setSession_id(rs.getInt("session_id"));
        dto.setSession_time(rs.getTime("session_time").toLocalTime());
        dto.setSession_date(rs.getTimestamp("session_date").toLocalDateTime().toLocalDate());
        dto.setMovie_title(rs.getString("movie_title"));
        Array pgArray = rs.getArray("occupied_seats");
        dto.setOccupied_seats(pgArray != null ? Arrays.asList((Integer[]) pgArray.getArray()) : Collections.emptyList());
        return dto;
    }

}