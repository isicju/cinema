package com.vkatit.cinema.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vkatit.cinema.config.QueryConfig;
import com.vkatit.cinema.dto.Ticket;
import com.vkatit.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TicketService {

    private final QueryConfig queryConfig;
    private final TicketRepository ticketRepository;

    public TicketService(
            QueryConfig queryConfig,
            TicketRepository ticketRepository) {
        this.queryConfig = queryConfig;
        this.ticketRepository = ticketRepository;
    }

    private Ticket getTicketData(int ticketId) {
        return ticketRepository.getTicket(queryConfig.getSqlQueries().get("getTicket"), ticketId);
    }

    public byte[] generatePdf(int ticketId) throws DocumentException {
        Ticket ticketData = getTicketData(ticketId);
        var byteArrayInputStream = new ByteArrayOutputStream();
        var document = new Document();
        PdfWriter.getInstance(document, byteArrayInputStream);
        document.open();
        drawSeatMap(document, ticketData.getSeatId());
        String movieTitle = ticketData.getMovieTitle();
        String firstName = ticketData.getFirstName();
        LocalDate sessionDate = ticketData.getSessionDate();
        LocalTime sessionTime = ticketData.getSessionTime();
        int seat = ticketData.getSeatId();
        addText(document, movieTitle, firstName, sessionDate, sessionTime, seat);
        document.close();
        return byteArrayInputStream.toByteArray();
    }

    private void addText(Document document, String movieTitle, String firstName, LocalDate sessionDate, LocalTime sessionTime, int seat) throws DocumentException {
        document.add(new Paragraph("Movie: " + movieTitle));
        document.add(new Paragraph("First name: " + firstName));
        document.add(new Paragraph("Session date: " + sessionDate));
        document.add(new Paragraph("Session time: " + sessionTime));
        document.add(new Paragraph("Seat: " + seat));
        document.add(new Paragraph("\n"));
    }

    private void drawSeatMap(Document document, int selectedSeat) throws DocumentException {
        var table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.getDefaultCell().setFixedHeight(20);
        var screenCell = new PdfPCell();
        screenCell.setColspan(10);
        screenCell.setFixedHeight(100);
        var screenText = new Paragraph("SCREEN", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
        screenText.setAlignment(Element.ALIGN_CENTER);
        screenCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        screenCell.addElement(screenText);
        table.addCell(screenCell);
        for (int row = 0; row < 5; row++) {
            for (int seat = 1; seat <= 10; seat++) {
                var cell = new PdfPCell();
                cell.setFixedHeight(20);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (row * 10 + seat == selectedSeat) {
                    cell.setBackgroundColor(BaseColor.GREEN);
                }
                cell.setPhrase(new Phrase(Integer.toString(row * 10 + seat)));
                table.addCell(cell);
            }
        }
        document.add(table);
    }

}