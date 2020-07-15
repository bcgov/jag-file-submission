package ca.bc.gov.open.jag.efilingapi.submission.service.submissionStore;

import ca.bc.gov.open.jag.efilingapi.api.model.ClientApplication;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@DisplayName("Submission Service: Put test suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutTest {

    @InjectMocks
    private SubmissionStoreImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("CASE 1: with valid submission should store submission")
    public void withValidObjectShouldPut() {

        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setType("type");
        Submission submission = new Submission.Builder().clientApplication(clientApplication).create();
        Optional<Submission> actual = sut.put(submission);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals("type", actual.get().getClientApplication().getType());

    }

}
