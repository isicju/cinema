package com.vkatit.cinema;

import com.vkatit.cinema.controller.DownloadController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class CinemaApplicationTests {

	@Autowired
	ApplicationContext context;

	@Autowired
	private DownloadController downloadController;

	@Value("${ticket.path}")
	private String TICKET_PATH;

	@Test
	void contextLoads() {
		assertTrue(context != null);
	}

	@Test
	@DirtiesContext
	public void fileDownload() {
		String fileName = "ticketid.pdf";
		Path filePath = Paths.get(TICKET_PATH, fileName);
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
	@DirtiesContext
	public void fileDownloadNotFound() {
		String fileName = "nonExistentFile.pdf";
		ResponseEntity<?> responseEntity = downloadController.download(fileName);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

}
