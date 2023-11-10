package com.vkatit.cinema;

import com.vkatit.cinema.controllers.HelloWorldController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        HelloWorldController homeController = new HelloWorldController();
        String result = homeController.hello();
        assertThat(result).isEqualTo("Hello World!");
    }
}
