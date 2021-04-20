package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerTest {

    AuthenticationController sut;

    @BeforeAll
    public void beforeAll() {

        sut = new AuthenticationController();

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withRequestReturnResult() {

        ResponseEntity actual = sut.postAuth(new Object());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

}
