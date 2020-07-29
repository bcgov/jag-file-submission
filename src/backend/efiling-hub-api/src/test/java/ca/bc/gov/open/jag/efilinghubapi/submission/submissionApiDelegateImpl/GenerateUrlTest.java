package ca.bc.gov.open.jag.efilinghubapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilinghub.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilinghubapi.submission.SubmissionApiDelegateImpl;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GenerateUrlTest {
    private SubmissionApiDelegateImpl sut;
    @BeforeAll
    public void setUp() {
        sut = new SubmissionApiDelegateImpl();
    }

    @Test
    @DisplayName("200: Valid request should generate a URL")
    public void withValidRequestShouldGenerateUrl() {
        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(null, null, null);
        Assertions.assertNotNull(actual.getBody());
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
    }
}
