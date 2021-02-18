package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
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
    public void testDiligenDocumentException() {

        ResponseEntity<Object> result = sut.handleDiligenDocumentException(new DiligenDocumentException("Something went wrong"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("Something went wrong", result.getBody());

    }

    @Test
    public void testDiligenAuthenticationException() {

        ResponseEntity<Object> result = sut.handleDiligenAuthenticationException(new DiligenAuthenticationException("Not authorized"), webRequestMock);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        Assertions.assertEquals("Not authorized", result.getBody());

    }
}
