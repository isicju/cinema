package com.vkatit.cinema.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import com.vkatit.cinema.model.Ticket;
import com.vkatit.cinema.service.Printable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/pdf")
public class PdfController {

    private final Printable printable;

    public PdfController(Printable printable) {
        this.printable = printable;
    }

    @GetMapping("/download")
    public ResponseEntity<?> download() {
        int id = 123;
        byte seat = 33;
        LocalDate date = LocalDate.of(2023, 11, 3);
        LocalTime time = LocalTime.of(12, 0);
        String movie = "Scenic Route";
        String firstName = "John";
        String lastName = "Wilson";
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setSeat(seat);
        ticket.setMovie(movie);
        ticket.setDate(date);
        ticket.setTime(time);
        ticket.setFirstName(firstName);
        ticket.setLastName(lastName);
        byte[] pdfContent = printable.getTicket(ticket);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        headers.setContentDispositionFormData("attachment", "Ticket-" + id + ".pdf");
        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

}