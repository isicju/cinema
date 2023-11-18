package com.vkatit.cinema.service;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogExample {

    public void logAllScenarios() {
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        log.fatal("fatal");
    }

}
