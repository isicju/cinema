package com.vkatit.cinema.service;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.vkatit.cinema.model.Printable;
import com.vkatit.cinema.model.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFEditor implements Printable {
    @Value("${rawTicketPath}")
    private String rawTicketPath;
    @Value("${generatedTicketPath}")
    private String outputFilePath;

    public PDFEditor(@Value("${rawTicketPath}") String rawTicketPath,
                     @Value("${generatedTicketPath}") String outputFilePath) {
        this.rawTicketPath = rawTicketPath;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public byte[] getTicket(Ticket ticket) {
        return handleExceptionsAndReturnBytesArray(ticket);
    }

    @ExceptionHandler
    public byte[] handleExceptionsAndReturnBytesArray(Ticket ticket) {
        try {
            createDocument(ticket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return getBytes(ticket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDocument(Ticket ticket) throws IOException {
        PdfDocument pdfDocument = createTicketPdf(ticket);
        fillOutTheFieldsOfTicket(new Document(pdfDocument), ticket);
        pdfDocument.close();
        getBytes(ticket);
    }

    public PdfDocument createTicketPdf(Ticket ticket) throws IOException {
        PdfReader reader = new PdfReader(rawTicketPath + ticket.getSeat() + ".pdf");
        PdfWriter writer = new PdfWriter(outputFilePath + ticket.getSeat() + ".pdf");
        return new PdfDocument(reader, writer);
    }

    public void fillOutTheFieldsOfTicket(Document document, Ticket ticket) throws IOException {
        PdfFont timesRomanFont;
        timesRomanFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        PdfPage page = document.getPdfDocument().getPage(1);
        fillOutTheDate(ticket, page, timesRomanFont);
        fillOutTheSeat(ticket, page, timesRomanFont);
        fillOutTheSite(page, timesRomanFont);
        fillOutMovieName(ticket, page, timesRomanFont);
        fillOutViewerName(ticket, page, timesRomanFont);
        fillOutViewerLastName(ticket, page, timesRomanFont);
    }

    public void fillOutTheDate(Ticket ticket, PdfPage page, PdfFont font) {
        Text date = new Text(ticket.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).setFont(font);
        getNormalCanvas(page, date, 295, 144, 43);
    }

    public void fillOutTheSeat(Ticket ticket, PdfPage page, PdfFont font) {
        Text seat = new Text(String.valueOf(ticket.getSeat())).setFont(font);
        getNormalCanvas(page, seat, 295, 76, 43);
    }

    public void fillOutTheSite(PdfPage page, PdfFont font) {
        Text site = new Text("powered by govnokodik.com").setFont(font);
        getRotatedCanvas(page, site, 1300, 350);
    }

    public void fillOutMovieName(Ticket ticket, PdfPage page, PdfFont font) {
        Text movieName;
        float y;
        float fontSize;
        if (ticket.getMovieName().length() <= 23) {
            movieName = new Text("Film: \n " + ticket.getMovieName()).setFont(font);
            y = 160;
            fontSize = 40;
        } else {
            List<String> movieNameParts = new ArrayList<>(List.of(ticket.getMovieName().split(" ")));
            StringBuilder builtString = new StringBuilder();
            for (String movieNamePart : movieNameParts) {
                builtString.append(movieNamePart).append(" ");
                if (builtString.length() > 23) {
                    builtString.append("\n");
                }
            }
            movieName = new Text("Film: \n " + builtString).setFont(font);
            y = 144;
            fontSize = 30;
        }
        getNormalCanvas(page, movieName, 1370, y, fontSize);
    }

    public void fillOutViewerName(Ticket ticket, PdfPage page, PdfFont font) {
        Text viewerName = new Text("Name: " + ticket.getViewer().getFirstName()).setFont(font);
        getNormalCanvas(page, viewerName, 1370, 500, 43);
    }

    public void fillOutViewerLastName(Ticket ticket, PdfPage page, PdfFont font) {
        Text viewerLastName = new Text("Last Name: " + ticket.getViewer().getLastName()).setFont(font);
        getNormalCanvas(page, viewerLastName, 1370, 450, 43);
    }


    private void getNormalCanvas(PdfPage page, Text text, float x, float y, float fontSize) {
        Paragraph paragraph = new Paragraph().setFontSize(fontSize).add(text);
        Canvas canvas = new Canvas(page, PageSize.A5);
        canvas.showTextAligned(paragraph, x, y, TextAlignment.LEFT);
    }

    private void getRotatedCanvas(PdfPage page, Text text, float x, float y) {
        Canvas canvas = new Canvas(page, PageSize.A5);
        canvas.showTextAligned(text.setFontSize(33).getText().toUpperCase(), x, y, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 7.85F);
    }

    public byte[] getBytes(Ticket ticket) throws IOException {
        Path filePath = Paths.get(outputFilePath + ticket.getSeat() + ".pdf");
        byte[] bytes;
        bytes = Files.readAllBytes(filePath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes);
        bytes = outputStream.toByteArray();
        return bytes;
    }
}
