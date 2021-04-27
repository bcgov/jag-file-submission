package ca.bc.gov.open.jag.aireviewermockapi.controller;

import ca.bc.gov.open.jag.aireviewermockapi.model.DocumentReady;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParentControllerTest {

    private static final String DOCUMENT_TYPE = "TYPE";
    private static final String RETURN_URI = "http://localhost:8080";
    @Mock
    RestTemplate restTemplateMock;

    ParentController sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new ParentController(restTemplateMock);

    }

    @Test
    @DisplayName("ok: POST document was retrieved")
    public void withValidRequestDocumentWasRetrieved() {

        Mockito.when(restTemplateMock.exchange(ArgumentMatchers.eq(RETURN_URI), any(), any(), any(Class.class))).thenReturn(ResponseEntity.ok("Success"));

        DocumentReady request = new DocumentReady();
        request.setDocumentId(BigDecimal.ONE);
        request.setDocumentType(DOCUMENT_TYPE);
        request.setReturnUri(RETURN_URI);

        ResponseEntity actual = sut.documentReady(request);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("bad request: POST  document was not retrieved")
    public void withInValidRequestDocumentWasNotRetrieved() {

        Mockito.when(restTemplateMock.exchange(ArgumentMatchers.eq(RETURN_URI), any(), any(), any(Class.class))).thenReturn(ResponseEntity.badRequest().body("Nope"));

        DocumentReady request = new DocumentReady();
        request.setDocumentId(BigDecimal.TEN);
        request.setDocumentType(DOCUMENT_TYPE);
        request.setReturnUri(RETURN_URI);

        ResponseEntity actual = sut.documentReady(request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());

    }

}
