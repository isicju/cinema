package com.vkatit.cinema.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SessionsMoviesSeatsDTO {

    @Column(name = "movie_id")
    private int movieId;
    @Column(name = "session_id")
    private int sessionId;
    @Column(name = "session_time")
    private LocalTime sessionTime;
    @Column(name = "session_date")
    private LocalDate sessionDate;
    @Column(name = "movie_title")
    private String movieTitle;
    @Column(name = "occupied_seats")
    private List<Integer> occupiedSeats;

}