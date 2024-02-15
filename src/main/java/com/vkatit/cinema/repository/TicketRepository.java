package com.vkatit.cinema.repository;

import com.vkatit.cinema.config.SqlQueries;
import com.vkatit.cinema.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TicketRepository {

    private final SqlQueries sqlQueries;
    private final JdbcTemplate jdbcTemplate;

    public Ticket getTicket(int ticketId) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(sqlQueries.GET_TICKET, BeanPropertyRowMapper.newInstance(Ticket.class), ticketId);
    }

}