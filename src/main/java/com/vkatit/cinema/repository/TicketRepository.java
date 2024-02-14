package com.vkatit.cinema.repository;

import com.vkatit.cinema.dto.Ticket;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TicketRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Ticket getTicket(String getTicketQuery, int ticketId) throws EmptyResultDataAccessException {
        SqlParameterSource parameters = new MapSqlParameterSource("ticket_id", ticketId);
        return namedParameterJdbcTemplate.queryForObject(
                getTicketQuery,
                parameters,
                new BeanPropertyRowMapper<>(Ticket.class));
    }

}