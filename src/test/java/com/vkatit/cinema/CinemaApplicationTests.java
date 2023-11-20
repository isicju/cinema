package com.vkatit.cinema;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.vkatit.cinema.model.Ticket;
import com.vkatit.cinema.model.Viewer;
import com.vkatit.cinema.service.PDFEditor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class CinemaApplicationTests {
    @Autowired
    ApplicationContext context;
    private String ticketText;
    @Value("${generatedTicketPath}")
    private String generatedTicket;

    private final Ticket ticket = new Ticket(LocalDateTime.now(),
            (int) ((Math.random() * 50) + 1),
            "Never gonna give you up", new Viewer("Rick", "Astley"));

    private final Ticket negativeTicket = new Ticket(LocalDateTime.of
            (1999, 2, 21, 12, 0),
            2, "",
            new Viewer("", ""));

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    public void getTicketPositiveTest() {
        context.getBean(PDFEditor.class).passTheTicket(ticket);
        ticketText = readTheTicketAndReturnString(ticket);
        Assertions.assertTrue(isDateFilledUp(ticketText));
        Assertions.assertTrue(isUserNameFilledUp(ticketText));
        Assertions.assertTrue(isMovieNameFilledUp(ticketText));
    }

    @Test
    public void getTicketNegativeTest() {
        context.getBean(PDFEditor.class).passTheTicket(negativeTicket);
        ticketText = readTheTicketAndReturnString(negativeTicket);
        Assertions.assertFalse(isUserNameFilledUp(ticketText));
        Assertions.assertFalse(isMovieNameFilledUp(ticketText));
    }

    public String readTheTicketAndReturnString(Ticket ticket) {
        String text;
        try {
            PdfReader reader = new PdfReader(generatedTicket + "Seat" + ticket.getSeat() + ".pdf");
            text = PdfTextExtractor.getTextFromPage(reader, 1);
            System.out.println(text);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    public boolean isDateFilledUp(String ticketText) {
        String dateRegex = "\\b\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}\\b";
        Matcher matcher = findTheMatchInString(ticketText, dateRegex);
        return matcher.find();
    }

    public boolean isUserNameFilledUp(String ticketText) {
        String nameRegex = "\\b(Name|Last Name):\\s\\w+\\b";
        Matcher matcher = findTheMatchInString(ticketText, nameRegex);
        return matcher.find();
    }

    public boolean isMovieNameFilledUp(String ticketText) {
        String movieNameRegex = "\\w+\\nFilm: \\n\\w+\\s\\w+";
        Matcher matcher = findTheMatchInString(ticketText, movieNameRegex);
        return matcher.find();
    }

    public Matcher findTheMatchInString(String ticketText, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(ticketText);
    }
}
