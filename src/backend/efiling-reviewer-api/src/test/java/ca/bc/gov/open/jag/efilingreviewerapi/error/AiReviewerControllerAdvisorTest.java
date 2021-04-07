package ca.bc.gov.open.jag.efilingreviewerapi.error;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ApiError;
import ca.bc.gov.open.jag.jagmailit.api.MailSendApi;
import ca.bc.gov.open.jag.jagmailit.api.handler.ApiException;
import ca.bc.gov.open.jag.jagmailit.api.model.EmailResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DiligenControllerAdvisor test suite")
public class AiReviewerControllerAdvisorTest {

    public static final String HELLO_EMAIL_COM = "hello@email.com";
    public AiReviewerControllerAdvisor sut;

    @Mock
    private WebRequest webRequestMock;

    @Mock
    private MailSendApi mailSendApiMock;

    @BeforeEach
    public void beforeEach() throws ApiException {
        MockitoAnnotations.openMocks(this);

        ErrorEmailProperties errorEmailProperties = new ErrorEmailProperties();

        errorEmailProperties.setFromEmail(HELLO_EMAIL_COM);
        errorEmailProperties.setToEmail(HELLO_EMAIL_COM);

        sut = new AiReviewerControllerAdvisor(mailSendApiMock, errorEmailProperties);

    }

    @Test
    @DisplayName("400: Assert bad request returned")
    public void testDiligenDocumentException() {

        ResponseEntity<Object> result = sut.handleDiligenDocumentException(new DiligenDocumentException("Something went wrong"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("Something went wrong", result.getBody());

    }

    @Test
    @DisplayName("400: Assert bad request returned")
    public void testAiReviewerDocumentException() {

        ResponseEntity<Object> result = sut.handleAiReviewerDocumentException(new AiReviewerDocumentException("Issue with file"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("DOCUMENT_VALIDATION", ((ApiError)result.getBody()).getError());
        Assertions.assertEquals("Issue with file", ((ApiError)result.getBody()).getMessage());

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

        ResponseEntity<Object> result = sut.handleDocumentExtractVirusFoundException(new AiReviewerVirusFoundException("Virus found"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
        Assertions.assertEquals("VIRUS_FOUND", ((ApiError)result.getBody()).getError());
        Assertions.assertEquals("Virus found", ((ApiError)result.getBody()).getMessage());
    }

    @Test
    @DisplayName("500: Assert cache exception")
    public void testAiReviewerCacheException() {

        ResponseEntity<Object> result = sut.handleAiReviewerCacheException(new AiReviewerCacheException("Cache Error"), webRequestMock);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals("CACHE_UNAVAILABLE", ((ApiError)result.getBody()).getError());
        Assertions.assertEquals("Cache Error", ((ApiError)result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert document mismatch exception")
    public void testAiReviewerDocumentTypeMismatchException() {

        ResponseEntity<Object> result = sut.handleDocumentMismatchException(new AiReviewerDocumentTypeMismatchException("Document mismatch exception"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("DOCUMENT_TYPE_MISMATCH", ((ApiError)result.getBody()).getError());
        Assertions.assertEquals("Document mismatch exception", ((ApiError)result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert restricted document exception")
    public void testAiReviewerRestrictedDocumentException() throws ApiException {

        Mockito.when(mailSendApiMock.mailSend(any())).thenReturn(new EmailResponse());

        ResponseEntity<Object> result = sut.handleRestrictedDocumentException(new AiReviewerRestrictedDocumentException("Restricted Document exception"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("RESTRICTED_DOCUMENT", ((ApiError)result.getBody()).getError());
        Assertions.assertEquals("Restricted Document exception", ((ApiError)result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert restricted document exception api throws error")
    public void testAiReviewerRestrictedDocumentExceptionAPIError() throws ApiException {

        Mockito.doThrow(ApiException.class).when(mailSendApiMock).mailSend(any());

        ResponseEntity<Object> result = sut.handleRestrictedDocumentException(new AiReviewerRestrictedDocumentException("Restricted Document exception"), webRequestMock);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("RESTRICTED_DOCUMENT", ((ApiError)result.getBody()).getError());
        Assertions.assertEquals("Restricted Document exception", ((ApiError)result.getBody()).getMessage());

    }

}
