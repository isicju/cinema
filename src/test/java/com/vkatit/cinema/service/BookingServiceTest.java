package com.vkatit.cinema.service;

import com.vkatit.cinema.dto.SessionsMoviesSeatsDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class BookingServiceTest {

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
    @Order(1)
    @DisplayName("Test of normal loading of the Spring context")
    void contextLoads() {
        assertThat(context, Matchers.notNullValue());
    }

    @Test
    @Order(2)
    @DisplayName("Test for retrieving sessions, movies and a list of seats")
    void fetchTest() {
        ParameterizedTypeReference<List<SessionsMoviesSeatsDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<SessionsMoviesSeatsDTO>> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/fetch",
                HttpMethod.GET,
                null,
                responseType);
        assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(responseEntity.getBody().get(0).getMovieId(), Matchers.is(1));
        assertThat(responseEntity.getBody().get(0).getSessionId(), Matchers.is(1));
        assertThat(responseEntity.getBody().get(0).getSessionDate(), Matchers.is(LocalDate.of(2023, 1, 1)));
        assertThat(responseEntity.getBody().get(0).getSessionTime(), Matchers.is(LocalTime.of(10, 0)));
        assertThat(responseEntity.getBody().get(0).getMovieTitle(), Matchers.is("Movie"));
        assertThat(responseEntity.getBody().get(0).getOccupiedSeats(), Matchers.is(List.of(1)));
    }

    @Test
    @Order(3)
    @DisplayName("Test of inserting user and ticket data and receiving ticket ID")
    void bookingTest() {
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/booking?firstname=NewName&lastname=NewLastName&email=new@email.com&sessionid=1&seatid=2",
                null,
                Integer.class);
        assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), Matchers.is(2));
    }

}