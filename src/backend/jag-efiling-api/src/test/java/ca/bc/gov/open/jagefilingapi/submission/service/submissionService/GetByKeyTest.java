package ca.bc.gov.open.jagefilingapi.submission.service.submissionService;


import ca.bc.gov.open.api.model.DocumentProperties;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetByKeyTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String TYPE = "type";
    public static final UUID CASE_2 = UUID.randomUUID();

    @InjectMocks
    private SubmissionServiceImpl sut;

//    @Mock
//    public StorageService<Submission> storageServiceMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        DocumentProperties documentMetadata = new DocumentProperties();
        documentMetadata.setType(TYPE);
     //   Mockito.when(storageServiceMock.getByKey(ArgumentMatchers.eq(ID))).thenReturn(Submission.builder().documentProperties(documentMetadata).create());

    }

    @Test
    @DisplayName("CASE 1: with valid id should return submission")
    public void withExistingIdShouldReturnSubmission() {

        Optional<Submission> actual = sut.getByKey(ID);

        Assertions.assertEquals(TYPE, actual.get().getDocumentProperties().getType());

    }

    @Test
    @DisplayName("CASE 2: with not found object should return null")
    public void withMissingObjectShouldReturnNull() {

        Optional<Submission> actual = sut.getByKey(CASE_2);

        Assertions.assertFalse(actual.isPresent());

    }




}
