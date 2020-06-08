package ca.bc.gov.open.jagefilingapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloControllerTest {
    @DisplayName("Test Hello")
    @Test
    public void testHell() {
        HelloController helloController = new HelloController();

        ResponseEntity<String> res = helloController.hello();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("Hello there", res.getBody());
    }
}
