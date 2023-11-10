package com.vkatit.cinema;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CinemaApplicationTests {

    @Autowired
    ApplicationContext context;
    @Value("${log.path}")
    private String LOG_PATH;

    private final Logger LOGGER = LogManager.getLogger(CinemaApplication.class);
    private final String TRACE_LEVEL = "TRACE";
    private final String DEBUG_LEVEL = "DEBUG";
    private final String INFO_LEVEL = "INFO";
    private final String WARN_LEVEL = "WARN";
    private final String ERROR_LEVEL = "ERROR";
    private final String FATAL_LEVEL = "FATAL";
    private final String LOG_FILE_INFO = "-info.log";
    private final String LOG_FILE_ERROR = "-error.log";

    @Test
    void contextLoads() {
        assertTrue(context != null);
    }

    @Test
    @DirtiesContext
    void traceLog() {
        String logLevel = TRACE_LEVEL;
        LOGGER.trace(message(logLevel));
        comparison(logLevel, message(logLevel));
    }

    @Test
    @DirtiesContext
    void debugLog() {
        String logLevel = DEBUG_LEVEL;
        LOGGER.debug(message(logLevel));
        comparison(logLevel, message(logLevel));
    }

    @Test
    @DirtiesContext
    void infoLog() {
        String logLevel = INFO_LEVEL;
        LOGGER.info(message(logLevel));
        comparison(logLevel, message(logLevel));
    }

    @Test
    @DirtiesContext
    void warnLog() {
        String logLevel = WARN_LEVEL;
        LOGGER.warn(message(logLevel));
        comparison(logLevel, message(logLevel));
    }

    @Test
    @DirtiesContext
    void errorLog() {
        String logLevel = ERROR_LEVEL;
        LOGGER.error(message(logLevel));
        comparison(logLevel, message(logLevel));
    }

    @Test
    @DirtiesContext
    void fatalLog() {
        String logLevel = FATAL_LEVEL;
        LOGGER.fatal(message(logLevel));
        comparison(logLevel, message(logLevel));
    }

    private void comparison(String logLevel, String message) {
        String logFile = LOG_PATH + LocalDate.now() +
                ((logLevel.equalsIgnoreCase(ERROR_LEVEL) || logLevel.equalsIgnoreCase(FATAL_LEVEL)) ? LOG_FILE_ERROR : LOG_FILE_INFO);
        timeForLoggerToWriteLog();
        Path path = Paths.get(logFile);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(lines.stream().anyMatch(line -> line.contains(message)));
    }

    void timeForLoggerToWriteLog() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String message(String logLevel) {
        return logLevel + " message";
    }

}