package com.vkatit.cinema.controller;

import com.itextpdf.text.DocumentException;
import com.vkatit.cinema.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/download/{ticketId}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable int ticketId) throws EmptyResultDataAccessException, DocumentException {
        byte[] pdfByteArray = ticketService.generateTicket(ticketId);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ticket-" + ticketId + ".pdf");
        return new ResponseEntity<>(pdfByteArray, headers, HttpStatus.OK);
    }

}