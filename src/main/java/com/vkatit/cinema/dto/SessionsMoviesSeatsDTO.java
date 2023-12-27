package com.vkatit.cinema.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SessionsMoviesSeatsDTO {

    private int movie_id;
    private int session_id;
    private LocalTime session_time;
    private LocalDate session_date;
    private String movie_title;
    private List<Integer> occupied_seats;

}