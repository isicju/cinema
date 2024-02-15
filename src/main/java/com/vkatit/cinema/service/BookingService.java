package com.vkatit.cinema.service;

import com.vkatit.cinema.model.BookingData;
import com.vkatit.cinema.model.SessionsMoviesSeats;
import com.vkatit.cinema.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private final DataSource dataSource;
    private final BookingRepository bookingRepository;

    public List<SessionsMoviesSeats> fetchData() {
        return bookingRepository.getSessionsMoviesSeats();
    }

    public int performBooking(BookingData bookingData) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            int userId = bookingRepository.insertUser(bookingData);
            int ticketId = bookingRepository.insertTicket(bookingData.getSessionId(), bookingData.getSeatId(), userId);
            connection.commit();
            return ticketId;
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            return 0;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

}