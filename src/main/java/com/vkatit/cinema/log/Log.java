package com.vkatit.cinema.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.time.LocalDate;

@Component
public class Log {

    @Value("${log.path}")
    private String PATH;
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    public void logging(Level level, String message, Throwable throwable) {
        try {
            FileHandler fileHandler = new FileHandler(getLogFileName(level), true);
            printLog(level, message, throwable, fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLogFileName(Level level) {
        String fileName = PATH + LocalDate.now() + (level == Level.SEVERE ? "-error.log" : "-info.log");
        File logFile = new File(fileName);
        logFile.getParentFile().mkdirs();
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    private void printLog(Level level, String message, Throwable throwable, FileHandler fileHandler) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement callingMethod = stackTrace[2];
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(level);
        String logMessage = "\n" + "Message: " + message + "\n" + "Calling Method: " + callingMethod + "\n" + throwableToString(throwable);
        logger.log(level, logMessage);
        fileHandler.close();
    }

    private String throwableToString(Throwable throwable) {
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            return sw.toString();
        }
        return "";
    }

}


