package ca.bc.gov.open.jagefilingapi.submission.service.submissionService;

import ca.bc.gov.open.api.model.DocumentProperties;
import ca.bc.gov.open.jagefilingapi.cache.StorageService;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@DisplayName("Submission Service: Put test suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutTest {

    private static final String ID = "id";

    @InjectMocks
    private SubmissionServiceImpl sut;

    @Mock
    public StorageService<Submission> storageServiceMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(storageServiceMock.put(Mockito.any())).thenReturn(ID);

    }

    @Test
    @DisplayName("CASE 1: with valid submission should store submission")
    public void withValidObjectShouldPut() {

        DocumentProperties documentMetadata = new DocumentProperties();
        Submission submission = new Submission.Builder().documentProperties(documentMetadata).create();
        String actual = sut.put(submission);
        Assertions.assertEquals(ID, actual);

    }

}
