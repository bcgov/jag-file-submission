package ca.bc.gov.open.jagefilingapi.submission.service.submissionService;


import ca.bc.gov.open.jagefilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetByKeyTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String TYPE = "type";

    @InjectMocks
    private SubmissionServiceImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        DocumentProperties documentMetadata = new DocumentProperties();
        documentMetadata.setType(TYPE);
    }

    @Test
    @DisplayName("CASE 1: without cache always return empty")
    public void withExistingIdShouldReturnSubmission() {

        Optional<Submission> actual = sut.getByKey(ID);
        Assertions.assertEquals(Optional.empty(), actual);

    }

    @Test
    @DisplayName("CASE 2: with put in cache should put return submission")
    public void withSubmissionShouldReturnOptional() {

        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setSubType("subtype");

        Submission submission = Submission
                .builder()
                .documentProperties(documentProperties)
                .create();
        Optional<Submission> actual = sut.put(submission);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals("subtype", actual.get().getDocumentProperties().getSubType());

    }


}
