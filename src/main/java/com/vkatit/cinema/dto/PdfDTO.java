package com.vkatit.cinema.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PdfDTO {

    @Column(name = "movie_title")
    private String movieTitle;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "session_date")
    private LocalDate sessionDate;
    @Column(name = "session_time")
    private LocalTime sessionTime;
    @Column(name = "seat_id")
    private int seatId;

}