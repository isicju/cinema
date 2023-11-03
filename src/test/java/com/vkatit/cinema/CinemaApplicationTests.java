package com.vkatit.cinema;

import com.vkatit.cinema.controller.PdfController;
import com.vkatit.cinema.model.Ticket;
import com.vkatit.cinema.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class CinemaApplicationTests {

	@Autowired
	ApplicationContext context;

	@MockBean
	private Printable printable;

	@Test
	void contextLoads() {
		assertTrue(context != null);
	}

	@Test
	public void pdfDownload() throws Exception {
		PdfController pdfController = new PdfController(printable);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(pdfController).build();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pdf/download"))
				.andExpect(status().isOk())
				.andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"Ticket-123.pdf\""))
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
	}

	@Test
	public void getTicket() throws Exception {
		PdfGeneratorService pdfGeneratorService = new PdfGeneratorService();
		Assertions.assertEquals(null, pdfGeneratorService.getTicket(new Ticket()));

	}

	@Test
	void pdfAndDigitalDataComparison() {
		int id = 123;
		byte seat = 33;
		LocalDate date = LocalDate.of(2023, 11, 3);
		LocalTime time = LocalTime.of(12, 0);;
		String movie = "Scenic Route";
		String firstName = "John";
		String lastName = "Wilson";
		String ticketTextExpected = "ID: " + id + "Date: " + date + "Time: " + time + "Seat: " + seat + "First name: " + firstName + "Last name: " + lastName + "Movie: " + movie ;
		try {
			PdfToImageServiceTest.convertPdfToJpeg();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Conversion PDF to JPG error");
		}
		String ticketTextActual = ImageOcrServiceTest.performOcr("src/main/resources/Ticket-" + id + ".jpg");
		Assertions.assertEquals(ticketTextExpected, ticketTextActual);
	}

}