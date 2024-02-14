package com.vkatit.cinema.repository;

import com.vkatit.cinema.dto.SessionsMoviesSeats;
import com.vkatit.cinema.dto.SessionsMoviesSeatsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public BookingRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public List<SessionsMoviesSeats> getSessionsMoviesSeats(String sqlQuery) {
        return jdbcTemplate.query(sqlQuery, new SessionsMoviesSeatsRowMapper());
    }

    public int insertUserTicket(String postUser, String postTicket, String firstName, String lastName, String email, int sessionId, int seatId) {
        Optional<Integer> ticketId = transactionTemplate.execute(status -> {
            try {
                SqlParameterSource userParams = new MapSqlParameterSource()
                        .addValue("first_name", firstName)
                        .addValue("last_name", lastName)
                        .addValue("email", email);
                KeyHolder userKeyHolder = new GeneratedKeyHolder();
                namedParameterJdbcTemplate.update(postUser, userParams, userKeyHolder);
                int userId = Objects.requireNonNull(userKeyHolder.getKey()).intValue();
                SqlParameterSource ticketParams = new MapSqlParameterSource()
                        .addValue("session_id", sessionId)
                        .addValue("seat_id", seatId)
                        .addValue("user_id", userId);
                KeyHolder ticketKeyHolder = new GeneratedKeyHolder();
                namedParameterJdbcTemplate.update(postTicket, ticketParams, ticketKeyHolder);
                return Optional.of((Objects.requireNonNull(ticketKeyHolder.getKey())).intValue());
            } catch (Exception e) {
                status.setRollbackOnly();
                return Optional.of(0);
            }
        });
        return ticketId.get();
    }

}