package com.vkatit.cinema;

import lombok.extern.log4j.Log4j2;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Log4j2
@SpringBootTest
class CinemaApplicationTests {

    static {
        System.setProperty("tempLogPath", System.getProperty("java.io.tmpdir"));
    }

    @Autowired
    ApplicationContext context;
    private final String LOG_PATH = System.getProperty("java.io.tmpdir");
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
    void cleanLogFiles() throws IOException {
        cleanFile(LOG_FILE_INFO);
        cleanFile(LOG_FILE_ERROR);
    }

    @Test
    void keepLogsByDateInfo() throws IOException {
        log.info(message(INFO_LEVEL));
        assertThat(existFile(LOG_FILE_INFO), is(true));
    }

    @Test
    void infoFileIncludesAllExceptErrorAndLower() throws IOException {
        performAllLoggingLevels();
        assertThat(hasContent(TRACE_LEVEL, LOG_FILE_INFO), is(true));
        assertThat(hasContent(DEBUG_LEVEL, LOG_FILE_INFO), is(true));
        assertThat(hasContent(INFO_LEVEL, LOG_FILE_INFO), is(true));
        assertThat(hasContent(WARN_LEVEL, LOG_FILE_INFO), is(true));
        assertThat(hasContent(ERROR_LEVEL, LOG_FILE_INFO), is(false));
        assertThat(hasContent(FATAL_LEVEL, LOG_FILE_INFO), is(false));
    }

    @Test
    void keepLogsByDateError() throws IOException {
        log.error(message(ERROR_LEVEL));
        assertThat(existFile(LOG_FILE_ERROR), is(true));
    }

    @Test
    void errorFileIncludesOnlyErrorAndLower() throws IOException {
        performAllLoggingLevels();
        assertThat(hasContent(TRACE_LEVEL, LOG_FILE_ERROR), is(false));
        assertThat(hasContent(DEBUG_LEVEL, LOG_FILE_ERROR), is(false));
        assertThat(hasContent(INFO_LEVEL, LOG_FILE_ERROR), is(false));
        assertThat(hasContent(WARN_LEVEL, LOG_FILE_ERROR), is(false));
        assertThat(hasContent(ERROR_LEVEL, LOG_FILE_ERROR), is(true));
        assertThat(hasContent(FATAL_LEVEL, LOG_FILE_ERROR), is(true));
    }

    private void performAllLoggingLevels() {
        log.trace(message(TRACE_LEVEL));
        log.debug(message(DEBUG_LEVEL));
        log.info(message(INFO_LEVEL));
        log.warn(message(WARN_LEVEL));
        log.error(message(ERROR_LEVEL));
        log.fatal(message(FATAL_LEVEL));
    }

    private boolean existFile(String filename) throws IOException {
        try (Stream<Path> walkStream = Files.walk(Paths.get(LOG_PATH), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            Optional<Path> logFile = walkStream
                    .filter(path -> path.getFileName().toString().equals(filename))
                    .findFirst();
            return logFile.isPresent();
        }
    }

    private boolean hasContent(String logLevel, String logFileName) throws IOException {
        String logFileFullPath = LOG_PATH + logFileName;
        Path path = Paths.get(logFileFullPath);
        List<String> lines;
        lines = Files.readAllLines(path);
        return lines.stream().anyMatch(line -> line.contains(message(logLevel)));
    }

    private String message(String logLevel) {
        return logLevel + " message";
    }

    public void cleanFile(String fileName) throws IOException {
        File logFile = new File(LOG_PATH + fileName);
        if (logFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(logFile, false)) {
                fileWriter.write("");
            }
        }
    }

}