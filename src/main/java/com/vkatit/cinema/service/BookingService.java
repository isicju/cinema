package com.vkatit.cinema.service;

import com.vkatit.cinema.dto.SessionsMoviesSeatsDTO;
import com.vkatit.cinema.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final Map<String, String> sqlQueries;
    private final BookingRepository bookingRepository;

    public BookingService(
            @Value("${sql.file.path}") String sqlFilePath,
            BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.sqlQueries = loadQueries(sqlFilePath);
    }

    public List<SessionsMoviesSeatsDTO> fetchData() {
        return bookingRepository.getSessionsMoviesSeats(sqlQueries.get("getSessionsMoviesSeats"));
    }

    public int performBooking(String firstName, String lastName, String email, int sessionId, int seatId) {
        return bookingRepository.insertUserTicket(sqlQueries.get("postUser"), sqlQueries.get("postTicket"), firstName, lastName, email, sessionId, seatId);
    }

    private Map<String, String> loadQueries(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();
            String fileContent  = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            Yaml yaml = new Yaml();
            return yaml.load(fileContent );
        } catch (IOException e) {
            throw new RuntimeException("Error loading SQL queries from file: " + fileName, e);
        }
    }

}