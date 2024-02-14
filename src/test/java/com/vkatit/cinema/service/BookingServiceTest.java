package com.vkatit.cinema.service;

import com.vkatit.cinema.dto.BookingData;
import com.vkatit.cinema.dto.SessionsMoviesSeats;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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
    @DisplayName("Test of normal loading of the Spring context")
    void contextLoads() {
        assertThat(context, Matchers.notNullValue());
    }

    @Test
    @Order(1)
    @DisplayName("Test for retrieving sessions, movies and a list of seats")
    void fetchTest() {
        int movieId = 1;
        int sessionId = 1;
        LocalDate date = LocalDate.of(2023, 1, 1);
        LocalTime time = LocalTime.of(10, 0);
        String movieName = "Movie";
        List<Integer> occupiedSeats = List.of(1);
        ParameterizedTypeReference<List<SessionsMoviesSeats>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<SessionsMoviesSeats>> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/fetch",
                HttpMethod.GET,
                null,
                responseType);
        assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(responseEntity.getBody().get(0).getMovieId(), Matchers.is(movieId));
        assertThat(responseEntity.getBody().get(0).getSessionId(), Matchers.is(sessionId));
        assertThat(responseEntity.getBody().get(0).getSessionDate(), Matchers.is(date));
        assertThat(responseEntity.getBody().get(0).getSessionTime(), Matchers.is(time));
        assertThat(responseEntity.getBody().get(0).getMovieTitle(), Matchers.is(movieName));
        assertThat(responseEntity.getBody().get(0).getOccupiedSeats(), Matchers.is(occupiedSeats));
    }

    @Test
    @Order(2)
    @DisplayName("Test of inserting user and ticket data and receiving ticket ID")
    void bookingTest() {
        BookingData bookingData = new BookingData();
        bookingData.setFirstname("NewName");
        bookingData.setLastname("NewLastName");
        bookingData.setEmail("new@email.com");
        bookingData.setSessionId(1);
        bookingData.setSeatId(2);
        HttpEntity<BookingData> requestEntity = new HttpEntity<>(bookingData);
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/booking",
                requestEntity,
                Integer.class);
        assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), Matchers.is(2));
    }

}