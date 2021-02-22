package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.exceptions.DocumentExtractVirusFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenControllerAdvisor test suite")
public class DiligenControllerAdvisorTest {

    public DiligenControllerAdvisor sut;

    @Mock
    private WebRequest webRequestMock;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        sut = new DiligenControllerAdvisor();

    }

    @Test
    @DisplayName("400: Assert bad request returned")
    public void testDiligenDocumentException() {

        ResponseEntity<Object> result = sut.handleDiligenDocumentException(new DiligenDocumentException("Something went wrong"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("Something went wrong", result.getBody());

    }

    @Test
    @DisplayName("500: Assert internal server error returned")
    public void testDiligenAuthenticationException() {

        ResponseEntity<Object> result = sut.handleDiligenAuthenticationException(new DiligenAuthenticationException("Not authorized"), webRequestMock);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals("Not authorized", result.getBody());

    }

    @Test
    @DisplayName("502: Assert bad gateway returned")
    public void testDocumentExtractVirusFoundException() {

        ResponseEntity<Object> result = sut.handleDocumentExtractVirusFoundException(new DocumentExtractVirusFoundException("Virus found"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
        Assertions.assertEquals("Virus found", result.getBody());

    }

}
