package com.vkatit.cinema.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class LogService {

    private final Logger LOGGER = LogManager.getLogger(LogService.class);

    public void performAllLogScenarios() {
        traceLog();
        debugLog();
        infoLog();
        warnLog();
        errorLog();
        fatalLog();
    }

    void traceLog() {
        byte seat = 33;
        LOGGER.trace("The user chose seat number " + seat);
    }

    void debugLog() {
        String customer = "John";
        LOGGER.debug("The ordering process for customer " + customer + " has begun");
        boolean orderingIsSuccessful = true;
        LOGGER.debug("The ordering process for " + customer + " is successful: " + orderingIsSuccessful);
    }

    void infoLog() {
        boolean authenticationResult = true;
        if (authenticationResult) {
            LOGGER.info("Authentication successful");
        } else {
            LOGGER.info("Authentication failed");
        }
    }

    void warnLog() {
        final int API_MAX_REQUESTS_PER_MINUTE = 100;
        int getAverageRequestsPerMinute = 95;
        if(getAverageRequestsPerMinute > API_MAX_REQUESTS_PER_MINUTE - 10) {
            LOGGER.warn("Number of API requests over 90% of possible throughput");
        }
    }

    void errorLog() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        try {
            arrayList.get(10);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error(e);
        }
    }

    void fatalLog() {
            try {
                divideByZero(10, 0);
            } catch (ArithmeticException e) {
                LOGGER.fatal(e);
                System.exit(1);
            }
    }

    private static int divideByZero(int a, int b) {
        return a / b;
    }

}
