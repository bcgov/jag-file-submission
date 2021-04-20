package ca.bc.gov.open.jag.aireviewermockapi.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectControllerTest {

    ProjectController sut;

    @BeforeAll
    public void beforeAll() {

        sut = new ProjectController();

    }

    @Test
    @DisplayName("ok: response returned ")
    public void withValidDetailsRequestReturnResult() {

        ResponseEntity actual = sut.postFile("1");

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("ok: valid response returned ")
    public void withRequestReturnValidResult() throws IOException {

        ResponseEntity actual = sut.getFileId("", new HashMap<String, String>() {{
            put("filter[fileName]", "test-valid-document.pdf");
        }} );

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("ok: invalid response returned ")
    public void withRequestReturnInValidResult() throws IOException {

        ResponseEntity actual = sut.getFileId("", new HashMap<String, String>() {{
            put("filter[fileName]", "test-invalid-document.pdf");
        }} );


        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("not found: with invalid id return not found ")
    public void withRequestReturnNotFound() throws IOException {

        ResponseEntity actual = sut.getFileId("", new HashMap<String, String>() {{
            put("filter[fileName]", "hello.pdf");
        }} );


        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

}
