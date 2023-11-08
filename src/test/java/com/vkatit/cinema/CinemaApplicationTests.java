package com.vkatit.cinema;

import com.vkatit.cinema.log.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.time.LocalDate;
import java.util.logging.Level;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CinemaApplicationTests {

    @Autowired
    ApplicationContext context;

    @Autowired
    private Log log;

    @Value("${log.path}")
    private String PATH;

    @Test
    void contextLoads() {
        assertTrue(context != null);
    }

    @Test
    @DirtiesContext
    public void loggingInfo() {
        Level level = Level.INFO;
        String message = "INFO logger test";
        log.logging(level, message, null);
        comparisonFileNameAndMessage(level, message);
    }

    @Test
    public void loggingError() {
        String message = "SEVERE logger test";
        Level level = Level.SEVERE;
        try {
            throw new RuntimeException("Exception from Try block");
        } catch (Exception e) {
            log.logging(level, message, e);
        }
        comparisonFileNameAndMessage(level, message);
    }

    public void comparisonFileNameAndMessage(Level expectedLevel, String expectedMessage) {
        LocalDate currentDate = LocalDate.now();
        String expectedFileName = PATH + currentDate + (expectedLevel == Level.SEVERE ? "-error.log" : "-info.log");
        assertTrue(new File(expectedFileName).exists());
        boolean found = false;
        try {
            Path path = FileSystems.getDefault().getPath(expectedFileName);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.contains(expectedMessage)) {
                    found = true;
                    System.out.println("Message \"" + expectedMessage + "\" - found");
                    break;
                }
            }
            assertTrue(found, "String \"" + expectedMessage + "\" was not found in the log file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}