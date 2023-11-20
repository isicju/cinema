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

@Service
public class PDFEditor {
    @Value("${rawTicketPath}")
    private String rawTicketPath;
    @Value("${generatedTicketPath}")
    private String outputFilePath;
    @Value("${siteAddressToPutInsideTicket}")
    private String siteAddress;

    public PDFEditor(@Value("${rawTicketPath}") String rawTicketPath,
                     @Value("${generatedTicketPath}") String outputFilePath) {
        this.rawTicketPath = rawTicketPath;
        this.outputFilePath = outputFilePath;
    }

    public byte[] passTheTicket(Ticket ticket) {
        return handleExceptionsAndReturnBytesArray(ticket);
    }

    @ExceptionHandler
    private byte[] handleExceptionsAndReturnBytesArray(Ticket ticket) {
        try {
            createDocument(ticket);
            return getBytes(ticket);
        } catch (IOException e) {
            System.out.println("exception occurred while trying to get ticket with seat number " + ticket.getSeat());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createDocument(Ticket ticket) throws IOException {
        PdfDocument pdfDocument = createTicketPdf(ticket);
        fillOutTheFieldsOfTicket(new Document(pdfDocument), ticket);
        pdfDocument.close();
        getBytes(ticket);
    }

    private PdfDocument createTicketPdf(Ticket ticket) throws IOException {
        PdfReader reader = new PdfReader(rawTicketPath);
        PdfWriter writer = new PdfWriter(outputFilePath + "Seat" + ticket.getSeat() + ".pdf");
        return new PdfDocument(reader, writer);
    }

    private void fillOutTheFieldsOfTicket(Document document, Ticket ticket) throws IOException {
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

    private void fillOutTheDate(Ticket ticket, PdfPage page, PdfFont font) {
        Text date = new Text(ticket.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).setFont(font);
        getNormalCanvas(page, date, 295, 144, 43);
    }

    private void fillOutTheSeat(Ticket ticket, PdfPage page, PdfFont font) {
        Text seat = new Text(String.valueOf(ticket.getSeat())).setFont(font);
        getNormalCanvas(page, seat, 295, 76, 43);
    }

    private void fillOutTheSite(PdfPage page, PdfFont font) {
        Text site = new Text(siteAddress).setFont(font);
        getRotatedCanvas(page, site);
    }

    private void fillOutMovieName(Ticket ticket, PdfPage page, PdfFont font) {
        Text movieName = new Text("Film: \n " + ticket.getMovieName()).setFont(font);
        getNormalCanvas(page, movieName, 1370, 160, 40);
    }

    private void fillOutViewerName(Ticket ticket, PdfPage page, PdfFont font) {
        Text viewerName = new Text("Name: " + ticket.getViewer().getFirstName()).setFont(font);
        getNormalCanvas(page, viewerName, 1370, 500, 43);
    }

    private void fillOutViewerLastName(Ticket ticket, PdfPage page, PdfFont font) {
        Text viewerLastName = new Text("Last Name: " + ticket.getViewer().getLastName()).setFont(font);
        getNormalCanvas(page, viewerLastName, 1370, 450, 43);
    }


    private void getNormalCanvas(PdfPage page, Text text, float x, float y, float fontSize) {
        Paragraph paragraph = new Paragraph().setFontSize(fontSize).add(text);
        new Canvas(page, PageSize.A5).showTextAligned(paragraph, x, y, TextAlignment.LEFT);
    }

    private void getRotatedCanvas(PdfPage page, Text text) {
        new Canvas(page, PageSize.A5).showTextAligned(text.setFontSize(33).getText().toUpperCase(),
                1300F, 350F, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 7.85F);
    }

    private byte[] getBytes(Ticket ticket) throws IOException {
        Path filePath = Paths.get(outputFilePath + "Seat" + ticket.getSeat() + ".pdf");
        byte[] bytes;
        bytes = Files.readAllBytes(filePath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(bytes);
        bytes = outputStream.toByteArray();
        return bytes;
    }
}