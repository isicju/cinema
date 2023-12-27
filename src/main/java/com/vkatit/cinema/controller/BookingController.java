package com.vkatit.cinema.controller;

import com.vkatit.cinema.dto.SessionsMoviesSeatsDTO;
import com.vkatit.cinema.service.BookingService;
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
    public ResponseEntity<List<SessionsMoviesSeatsDTO>> fetch() {
        List<SessionsMoviesSeatsDTO> sessionsMoviesSeats = bookingService.fetchData();
        if (sessionsMoviesSeats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sessionsMoviesSeats, HttpStatus.OK);
    }

    @PostMapping("/booking")
    public ResponseEntity<Integer> booking(
            @RequestParam(name = "firstname") String firstname,
            @RequestParam(name = "lastname") String lastname,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "sessionid") int sessionId,
            @RequestParam(name = "seatid") int seatId
    ) {
        int ticketId = bookingService.performBooking(firstname, lastname, email, sessionId, seatId);
        if (ticketId == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ticketId, HttpStatus.OK);
    }

}