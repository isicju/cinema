package com.vkatit.cinema.repository;

import com.vkatit.cinema.dto.PdfDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class PdfRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PdfRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public PdfDTO getTicket(String getTicketQuery, int ticketId) {
        SqlParameterSource parameters = new MapSqlParameterSource("ticket_id", ticketId);
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    getTicketQuery,
                    parameters,
                    new BeanPropertyRowMapper<>(PdfDTO.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}