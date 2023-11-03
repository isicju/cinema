package com.vkatit.cinema.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class Ticket {

    int id;
    byte seat;
    String movie;
    LocalDate date;
    LocalTime time;
    String firstName;
    String lastName;

    public void setId(int id) {
        this.id = id;
    }

    public void setSeat(byte seat) {
        this.seat = seat;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
