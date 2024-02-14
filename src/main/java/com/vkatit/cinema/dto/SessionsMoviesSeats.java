package com.vkatit.cinema.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SessionsMoviesSeats {

    private int movieId;
    private int sessionId;
    private LocalTime sessionTime;
    private LocalDate sessionDate;
    private String movieTitle;
    private List<Integer> occupiedSeats;

}