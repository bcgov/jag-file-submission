package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentControllerTest {

    DocumentController sut;

    @BeforeAll
    public void beforeAll() {

        sut = new DocumentController();

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withValidDetailsRequestReturnResult() {

        ResponseEntity actual = sut.getDetails(1234);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("ok: valid response returned ")
    public void withRequestReturnValidResult() throws IOException {

        ResponseEntity actual = sut.getFields(1234);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("ok: invalid response returned ")
    public void withRequestReturnInValidResult() throws IOException {

        ResponseEntity actual = sut.getFields(9999);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("not found: with invalid id return not found ")
    public void withRequestReturnNotFound() throws IOException {

        ResponseEntity actual = sut.getFields(1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }


}
