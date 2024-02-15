package com.vkatit.cinema.model;

import lombok.Data;

@Data
public class BookingData {

    private String firstname;
    private String lastname;
    private String email;
    private int sessionId;
    private int seatId;

}