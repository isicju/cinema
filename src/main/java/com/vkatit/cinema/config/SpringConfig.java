package com.vkatit.cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan("com.vkatit.cinema")
@PropertySource("classpath:application.properties")
public class SpringConfig {
    private final Environment environment;

    @Autowired
    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Connection dataSource() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(environment.getProperty("url"),
                    environment.getProperty("dbusername"), environment.getProperty("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}