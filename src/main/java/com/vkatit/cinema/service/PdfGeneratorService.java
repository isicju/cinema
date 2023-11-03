package com.vkatit.cinema.service.ticket;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PdfGeneratorService implements Printable {

    @Override
    public byte[] getTicket(String ticketName) {
        ticketName = "Ticket-123.pdf";
        ClassPathResource resource = new ClassPathResource(ticketName);
        try (InputStream inputStream = resource.getInputStream()) {
            Path tempFile = Files.createTempFile("temp-file", ".pdf");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return Files.readAllBytes(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
