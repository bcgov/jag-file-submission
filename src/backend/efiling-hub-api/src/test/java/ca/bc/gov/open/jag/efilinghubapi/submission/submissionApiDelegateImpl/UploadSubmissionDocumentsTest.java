package ca.bc.gov.open.jag.efilinghubapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilinghub.api.model.UploadSubmissionDocumentsResponse;
import ca.bc.gov.open.jag.efilinghubapi.submission.SubmissionApiDelegateImpl;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class UploadSubmissionDocumentsTest {
    private SubmissionApiDelegateImpl sut;
    @BeforeAll
    public void setUp() {
        sut = new SubmissionApiDelegateImpl();
    }
    @Test
    @DisplayName("200: Valid request should add documents")
    public void withValidRequestShouldUploadFiles() {
        ResponseEntity<UploadSubmissionDocumentsResponse> actual = sut.uploadSubmissionDocuments(null, null);
        Assertions.assertNotNull(actual.getBody());
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
    }
}

