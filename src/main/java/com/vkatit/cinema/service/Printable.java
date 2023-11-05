package com.vkatit.cinema.service;

import com.vkatit.cinema.model.Ticket;
import org.springframework.stereotype.Service;

@Service
public interface Printable {

    byte[] print(Ticket ticket);

}