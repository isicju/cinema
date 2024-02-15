package com.vkatit.cinema.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SessionsMoviesSeats {

    private int movieId;
    private int sessionId;
    private LocalTime sessionTime;
    private LocalDate sessionDate;
    private String movieTitle;
    private List<Integer> occupiedSeats;

}