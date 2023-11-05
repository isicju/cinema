package com.vkatit.cinema;

import com.vkatit.cinema.controller.DownloadController;
import com.vkatit.cinema.model.Ticket;
import com.vkatit.cinema.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class CinemaApplicationTests {

	@Autowired
	ApplicationContext context;

	@Autowired
	private DownloadController downloadController;

	@Test
	void contextLoads() {
		assertTrue(context != null);
	}

	@Test
	public void fileDownload() throws IOException {
		String fileName = "ticketid.pdf";
		Path filePath = Paths.get("src/main/resources/", fileName);
		try {
			Resource resource = new FileSystemResource(filePath.toFile());
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			ResponseEntity<Resource> response = (ResponseEntity<Resource>) downloadController.download(fileName);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(resource.contentLength(), response.getHeaders().getContentLength());
			assertEquals(headers.getContentDisposition(), response.getHeaders().getContentDisposition());
			assertEquals(headers.getContentType(), response.getHeaders().getContentType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fileDownloadNotFound() {
		String fileName = "nonExistentFile.pdf";
		ResponseEntity<?> responseEntity = downloadController.download(fileName);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

//	@Test
//	public void testDownloadFileError() {
//		String fileName = "fileWithError.pdf";
//		ResponseEntity<?> responseEntity = downloadController.download(fileName);
////		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//		String responseBody = (String) responseEntity.getBody();
//		assertEquals("File download error", responseBody);
//	}



	@Test
	void pdfAndDigitalDataComparison() {
		int id = 123;
		byte seat = 33;
		LocalDate date = LocalDate.of(2023, 11, 3);
		LocalTime time = LocalTime.of(12, 0);;
		String movie = "Scenic Route";
		String firstName = "John";
		String lastName = "Wilson";
				Ticket ticket = new Ticket.TicketBuilder()
				.setId(id)
				.setSeat(seat)
				.setMovie(movie)
				.setDate(date)
				.setTime(time)
				.setFirstName(firstName)
				.setLastName(lastName)
				.build();
		String ticketTextExpected = "ID: " + ticket.getId() + "Date: " + ticket.getDate() + "Time: " + ticket.getTime() + "Seat: " + ticket.getSeat() + "First name: " + ticket.getFirstName() + "Last name: " + ticket.getLastName() + "Movie: " + ticket.getMovie() ;
		try {
			PdfToImageServiceTest.convertPdfToJpeg();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Conversion PDF to JPG error");
		}
		String ticketTextActual = ImageOcrServiceTest.performOcr("src/main/resources/ticketid.jpg");
		Assertions.assertEquals(ticketTextExpected, ticketTextActual);
	}

}