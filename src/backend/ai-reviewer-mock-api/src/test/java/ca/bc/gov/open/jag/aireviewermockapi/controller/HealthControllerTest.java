package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HealthControllerTest {

    HealthController sut;

    @BeforeAll
    public void beforeAll() {

        sut = new HealthController();

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withRequestReturnResult() {

        ResponseEntity actual = sut.getHealth();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

}
