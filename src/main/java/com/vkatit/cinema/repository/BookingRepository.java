package com.vkatit.cinema.repository;

import com.vkatit.cinema.config.SqlQueries;
import com.vkatit.cinema.model.BookingData;
import com.vkatit.cinema.model.SessionsMoviesSeats;
import com.vkatit.cinema.model.SessionsMoviesSeatsRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookingRepository {

    private final SqlQueries sqlQueries;
    private final JdbcTemplate jdbcTemplate;

    public List<SessionsMoviesSeats> getSessionsMoviesSeats() {
        return jdbcTemplate.query(sqlQueries.GET_SESSIONS_MOVIES_SEATS, new SessionsMoviesSeatsRowMapper());
    }

    public int insertUser(BookingData bookingData) {
        Optional<Integer> userIdOptional = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQueries.INSERT_USER, Integer.class, bookingData.getFirstname(), bookingData.getLastname(), bookingData.getEmail()));
        return userIdOptional.orElse(0);
    }

    public int insertTicket(int sessionId, int seatId, int userId) {
        Optional<Integer> ticketIdOptional = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQueries.INSERT_TICKET, Integer.class, sessionId, seatId, userId));
        return ticketIdOptional.orElse(0);
    }

}