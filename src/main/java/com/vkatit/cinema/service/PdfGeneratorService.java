package com.vkatit.cinema.service;

import com.vkatit.cinema.model.Ticket;
import org.springframework.stereotype.Service;

@Service
public class PdfGeneratorService implements Printable {

    @Override
    public byte[] getTicket(Ticket ticket) {
        return null;
    }

}
