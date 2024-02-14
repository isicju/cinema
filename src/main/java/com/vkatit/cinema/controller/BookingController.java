package com.vkatit.cinema.controller;

import com.vkatit.cinema.dto.SessionsMoviesSeats;
import com.vkatit.cinema.dto.BookingData;
import com.vkatit.cinema.service.BookingService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<SessionsMoviesSeats>> fetch() {
        List<SessionsMoviesSeats> sessionsMoviesSeats = bookingService.fetchData();
        if (sessionsMoviesSeats.isEmpty()) {
            throw new EmptyResultDataAccessException(0);
        }
        return new ResponseEntity<>(sessionsMoviesSeats, HttpStatus.OK);
    }

    @PostMapping("/booking")
    public ResponseEntity<Integer> booking(@RequestBody BookingData bookingData) {
        int ticketId = bookingService.performBooking(bookingData);
        if (ticketId == 0) {
            throw new EmptyResultDataAccessException(0);
        }
        return new ResponseEntity<>(ticketId, HttpStatus.OK);
    }

}