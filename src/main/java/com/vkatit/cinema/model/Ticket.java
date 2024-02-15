package com.vkatit.cinema.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Ticket {

    private String movieTitle;
    private String firstName;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private int seatId;

}