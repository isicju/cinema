package com.vkatit.cinema.service;

import com.vkatit.cinema.dto.PdfDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;

@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PdfServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ApplicationContext context;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Test
    @DisplayName("Test of normal loading of the Spring context")
    void contextLoads() {
        assertThat(context, Matchers.notNullValue());
    }

    @Test
    @DisplayName("MANUAL Test for retrieving ticket - 200 OK")
    void getTicketOkManualTest() {
        System.out.println(port);
        ResponseEntity<PdfDTO> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/pdf/1",
                null,
                PdfDTO.class);
        assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
    }

    @Test
    @DisplayName("Test for retrieving ticket - 204 NO CONTENT")
    void getTicketError() {
        System.out.println(port);
        ResponseEntity<PdfDTO> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/pdf/2",
                null,
                PdfDTO.class);
        assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.NO_CONTENT));
    }

}