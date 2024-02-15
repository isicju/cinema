package com.vkatit.cinema.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vkatit.cinema.model.Ticket;
import com.vkatit.cinema.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private Ticket getTicketData(int ticketId) {
        return ticketRepository.getTicket(ticketId);
    }

    public byte[] generateTicket(int ticketId) throws DocumentException {
        Ticket ticket = getTicketData(ticketId);
        var byteArrayInputStream = new ByteArrayOutputStream();
        var document = new Document();
        try {
            PdfWriter.getInstance(document, byteArrayInputStream);
            document.open();
            drawTheaterMap(document, ticket.getSeatId());
            addText(document, ticket);
        } finally {
            document.close();
        }
        return byteArrayInputStream.toByteArray();
    }

    private void addText(Document document, Ticket ticket) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add("Movie: " + ticket.getMovieTitle());
        paragraph.add("\nFirst name: " + ticket.getFirstName());
        paragraph.add("\nSession date: " + ticket.getSessionDate());
        paragraph.add("\nSession time: " + ticket.getSessionTime());
        paragraph.add("\nSeat: " + ticket.getSeatId());
        paragraph.add("\n\n");
        document.add(paragraph);
    }

    private void drawTheaterMap(Document document, int selectedSeat) throws DocumentException {
        var pdfPTable = new PdfPTable(10);
        pdfPTable.setWidthPercentage(100);
        pdfPTable.getDefaultCell().setFixedHeight(20);
        var screenCell = new PdfPCell();
        screenCell.setColspan(10);
        screenCell.setFixedHeight(100);
        var screenTextParagraph = new Paragraph("SCREEN", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        screenTextParagraph.setAlignment(Element.ALIGN_CENTER);
        screenCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        screenCell.addElement(screenTextParagraph);
        pdfPTable.addCell(screenCell);
        drawSeats(pdfPTable, selectedSeat);
        document.add(pdfPTable);
    }

    private void drawSeats(PdfPTable pdfPTable, int selectedSeat) {
        for (int row = 0; row < 5; row++) {
            for (int seat = 1; seat <= 10; seat++) {
                var pdfPCell = new PdfPCell();
                pdfPCell.setFixedHeight(20);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (row * 10 + seat == selectedSeat) {
                    pdfPCell.setBackgroundColor(BaseColor.GREEN);
                }
                pdfPCell.setPhrase(new Phrase(Integer.toString(row * 10 + seat)));
                pdfPTable.addCell(pdfPCell);
            }
        }
    }

}