package com.vkatit.cinema.controller;

import com.vkatit.cinema.model.SessionsMoviesSeats;
import com.vkatit.cinema.model.BookingData;
import com.vkatit.cinema.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/fetch")
    public ResponseEntity<List<SessionsMoviesSeats>> fetch() {
        List<SessionsMoviesSeats> sessionsMoviesSeats = bookingService.fetchData();
        if (sessionsMoviesSeats.isEmpty()) {
            throw new EmptyResultDataAccessException(0);
        }
        return new ResponseEntity<>(sessionsMoviesSeats, HttpStatus.OK);
    }

    @PostMapping("/book")
    public ResponseEntity<Integer> book(@RequestBody BookingData bookingData) throws SQLException {
        int ticketId = bookingService.performBooking(bookingData);
        if (ticketId == 0) {
            throw new EmptyResultDataAccessException(0);
        }
        return new ResponseEntity<>(ticketId, HttpStatus.OK);
    }

}