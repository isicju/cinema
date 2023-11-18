package com.vkatit.cinema;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.FileVisitOption;
import java.time.LocalDate;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CinemaApplicationTests {

    @Autowired
    ApplicationContext context;
    private final String LOG_PATH = "rc/main/resources/log/";

    private final Logger LOGGER = LogManager.getLogger(CinemaApplication.class);
    private final String TRACE_LEVEL = "TRACE";
    private final String DEBUG_LEVEL = "DEBUG";
    private final String INFO_LEVEL = "INFO";
    private final String WARN_LEVEL = "WARN";
    private final String ERROR_LEVEL = "ERROR";
    private final String FATAL_LEVEL = "FATAL";
    private final String LOG_FILE_INFO = LocalDate.now() + "-info.log";
    private final String LOG_FILE_ERROR = LocalDate.now() + "-error.log";

    @Test
    void contextLoads() {
        assertTrue(context != null);
    }

    @AfterEach
    void cleanUp() {
        cleanLogFiles();
    }

    @Test
    @DirtiesContext
    void keepLogsByDateInfo() {
        LOGGER.info(message(INFO_LEVEL));
        assertTrue(findLogFileName(LOG_FILE_INFO));
    }

    @Test
    @DirtiesContext
    void infoFileIncludesAllExceptErrorAndLower() {
        performAllLoggingLevels();
        assertTrue(compareLogFileContent(TRACE_LEVEL, LOG_FILE_INFO));
        assertTrue(compareLogFileContent(DEBUG_LEVEL, LOG_FILE_INFO));
        assertTrue(compareLogFileContent(INFO_LEVEL, LOG_FILE_INFO));
        assertTrue(compareLogFileContent(WARN_LEVEL, LOG_FILE_INFO));
        assertFalse(compareLogFileContent(ERROR_LEVEL, LOG_FILE_INFO));
        assertFalse(compareLogFileContent(FATAL_LEVEL, LOG_FILE_INFO));
    }

    @Test
    @DirtiesContext
    void keepLogsByDateError() {
        LOGGER.error(message(ERROR_LEVEL));
        assertTrue(findLogFileName(LOG_FILE_INFO));
    }

    @Test
    @DirtiesContext
    void errorFileIncludesOnlyErrorAndLower() {
        performAllLoggingLevels();
        assertFalse(compareLogFileContent(TRACE_LEVEL, LOG_FILE_ERROR));
        assertFalse(compareLogFileContent(DEBUG_LEVEL, LOG_FILE_ERROR));
        assertFalse(compareLogFileContent(INFO_LEVEL, LOG_FILE_ERROR));
        assertFalse(compareLogFileContent(WARN_LEVEL, LOG_FILE_ERROR));
        assertTrue(compareLogFileContent(ERROR_LEVEL, LOG_FILE_ERROR));
        assertTrue(compareLogFileContent(FATAL_LEVEL, LOG_FILE_ERROR));
    }

    private void performAllLoggingLevels() {
        LOGGER.trace(message(TRACE_LEVEL));
        LOGGER.debug(message(DEBUG_LEVEL));
        LOGGER.info(message(INFO_LEVEL));
        LOGGER.warn(message(WARN_LEVEL));
        LOGGER.error(message(ERROR_LEVEL));
        LOGGER.fatal(message(FATAL_LEVEL));
    }

    private boolean findLogFileName(String filename) {
        try (Stream<Path> walkStream = Files.walk(Paths.get(LOG_PATH), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            Optional<Path> logFile = walkStream
                    .filter(path -> path.getFileName().toString().equals(filename))
                    .findFirst();
            return logFile.isPresent();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean compareLogFileContent(String logLevel, String logFileName) {
        String logFileFullPath = LOG_PATH + logFileName;
        timeForLoggerToWriteLog();
        Path path = Paths.get(logFileFullPath);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return lines.stream().anyMatch(line -> line.contains(message(logLevel)));
    }

    private void timeForLoggerToWriteLog() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String message(String logLevel) {
        return logLevel + " message";
    }

    public void cleanLogFiles() {
        cleanSpecificFile(LOG_FILE_INFO);
        cleanSpecificFile(LOG_FILE_ERROR);

    }

    public void cleanSpecificFile(String logFileName) {
        String logFilePath = LOG_PATH + logFileName;
        File logFile = new File(logFilePath);
        if (logFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(logFile, false)) {
                fileWriter.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The '" + logFilePath + "' file doesn't exist");
        }
    }

}