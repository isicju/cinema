package com.vkatit.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Ticket {
    private LocalDateTime date;
    private int seat;
    private String movieName;
    private Viewer viewer;
}