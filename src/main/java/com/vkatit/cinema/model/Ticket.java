package com.vkatit.cinema.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class Ticket {

    private int id;
    private byte seat;
    private String movie;
    private LocalDate date;
    private LocalTime time;
    private String firstName;
    private String lastName;

    private Ticket() {
    }

    public static class TicketBuilder {

        private int idBuilder;
        private byte seatBuilder;
        private String movieBuilder;
        private LocalDate dateBuilder;
        private LocalTime timeBuilder;
        private String firstNameBuilder;
        private String lastNameBuilder;

        public TicketBuilder setId(int idBuilder) {
            this.idBuilder = idBuilder;
            return this;
        }

        public TicketBuilder setSeat(byte seatBuilder) {
            this.seatBuilder = seatBuilder;
            return this;
        }

        public TicketBuilder setMovie(String movieBuilder) {
            this.movieBuilder = movieBuilder;
            return this;
        }

        public TicketBuilder setDate(LocalDate dateBuilder) {
            this.dateBuilder = dateBuilder;
            return this;
        }

        public TicketBuilder setTime(LocalTime timeBuilder) {
            this.timeBuilder = timeBuilder;
            return this;
        }

        public TicketBuilder setFirstName(String firstNameBuilder) {
            this.firstNameBuilder = firstNameBuilder;
            return this;
        }

        public TicketBuilder setLastName(String lastNameBuilder) {
            this.lastNameBuilder = lastNameBuilder;
            return this;
        }

        public Ticket build() {
            Ticket ticket = new Ticket();
            ticket.id = this.idBuilder;
            ticket.seat = this.seatBuilder;
            ticket.movie = this.movieBuilder;
            ticket.date = this.dateBuilder;
            ticket.time = this.timeBuilder;
            ticket.firstName = this.firstNameBuilder;
            ticket.lastName = this.lastNameBuilder;
            return ticket;
        }

    }

    public int getId() {
        return id;
    }

    public byte getSeat() {
        return seat;
    }

    public String getMovie() {
        return movie;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}