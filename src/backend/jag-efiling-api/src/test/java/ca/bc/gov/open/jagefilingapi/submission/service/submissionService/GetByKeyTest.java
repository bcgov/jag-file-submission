package ca.bc.gov.open.jagefilingapi.submission.service.submissionService;


import ca.bc.gov.open.api.model.SubmissionMetadata;
import ca.bc.gov.open.jagefilingapi.cache.StorageService;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetByKeyTest {

    private static final String ID = "id";
    private static final String TYPE = "type";
    public static final String CASE_2 = "CASE 2";

    @InjectMocks
    private SubmissionServiceImpl sut;

    @Mock
    public StorageService<Submission> storageServiceMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        SubmissionMetadata documentMetadata = new SubmissionMetadata();
        documentMetadata.setType(TYPE);
        Mockito.when(storageServiceMock.getByKey(Mockito.eq(ID), Mockito.any())).thenReturn(Submission.builder().submissionMetadata(documentMetadata).create());

    }

    @Test
    @DisplayName("CASE 1: with valid id should return submission")
    public void withExistingIdShouldReturnSubmission() {

        Optional<Submission> actual = sut.getByKey(ID);

        Assertions.assertEquals(TYPE, actual.get().getSubmissionMetadata().getType());

    }

    @Test
    @DisplayName("CASE 2: with not found object should return null")
    public void withMissingObjectShouldReturnNull() {

        Optional<Submission> actual = sut.getByKey(CASE_2);

        Assertions.assertFalse(actual.isPresent());

    }




}
