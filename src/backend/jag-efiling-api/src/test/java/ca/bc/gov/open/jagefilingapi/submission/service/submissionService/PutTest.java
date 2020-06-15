package ca.bc.gov.open.jagefilingapi.submission.service.submissionService;

import ca.bc.gov.open.api.model.DocumentProperties;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@DisplayName("Submission Service: Put test suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutTest {

    private static final String ID = "id";

    @InjectMocks
    private SubmissionServiceImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("CASE 1: with valid submission should store submission")
    public void withValidObjectShouldPut() {

        DocumentProperties documentMetadata = new DocumentProperties();
        documentMetadata.setType("type");
        Submission submission = new Submission.Builder().documentProperties(documentMetadata).create();
        Optional<Submission> actual = sut.put(submission);
        Assertions.assertEquals("type", actual.get().getDocumentProperties().getType());

    }

}
