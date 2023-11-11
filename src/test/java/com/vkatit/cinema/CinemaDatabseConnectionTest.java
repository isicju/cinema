package com.vkatit.cinema;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CinemaDatabseConnectionTest {

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        Connection connection = (Connection) context.getBean("dataSource");
        assertTrue(connection != null);
    }
}
