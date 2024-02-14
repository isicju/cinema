package com.vkatit.cinema.service;

import com.vkatit.cinema.config.QueryConfig;
import com.vkatit.cinema.dto.BookingData;
import com.vkatit.cinema.dto.SessionsMoviesSeats;
import com.vkatit.cinema.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final QueryConfig queryConfig;
    private final BookingRepository bookingRepository;

    public BookingService(
            QueryConfig queryConfig,
            BookingRepository bookingRepository) {
        this.queryConfig = queryConfig;
        this.bookingRepository = bookingRepository;
    }

    public List<SessionsMoviesSeats> fetchData() {
        return bookingRepository.getSessionsMoviesSeats(queryConfig.getSqlQueries().get("getSessionsMoviesSeats"));
    }

    public int performBooking(BookingData bookingData) {
        return bookingRepository.insertUserTicket(queryConfig.getSqlQueries().get("postUser"), queryConfig.getSqlQueries().get("postTicket"), bookingData.getFirstname(), bookingData.getLastname(), bookingData.getEmail(), bookingData.getSessionId(), bookingData.getSeatId());
    }

}