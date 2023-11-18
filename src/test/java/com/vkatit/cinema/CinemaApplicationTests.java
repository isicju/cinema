package com.vkatit.cinema;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CinemaApplicationTests {

	@Autowired
	ApplicationContext context;

	@Value("${ticket.path}")
	private String TICKET_PATH;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
		assertTrue(context != null);
	}

	@Test
	public void downloadOk() throws Exception {
		String fileName = "ticketid.pdf";
		String url = url(fileName);
		ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
		assertEquals("attachment; filename=" + fileName, response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
		assertArrayEquals(Files.readAllBytes(Paths.get(TICKET_PATH, fileName)), response.getBody());
	}

	@Test
	public void downloadFileNotFound() {
		String fileName = "nonExistentFile.pdf";
		String url = url(fileName);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	private String url(String fileName) {
		return  "http://localhost:" + port + "/api/v1/pdf/download/" + fileName;
	}

}
