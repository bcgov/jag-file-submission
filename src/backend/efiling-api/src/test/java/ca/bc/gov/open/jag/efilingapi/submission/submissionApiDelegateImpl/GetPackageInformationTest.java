package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.ClientApplication;

import ca.bc.gov.open.jag.efilingapi.api.model.Court;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.ModelPackage;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GetPackageInformationTest {

    private static final String DIVISION = "DIVISION";
    private static final String FILENUMBER = "FILENUMBER";
    private static final String LEVEL = "LEVEL";
    private static final String LOCATION = "LOCATION";
    private static final String PARTICIPATIONCLASS = "PARTICIPATIONCLASS";
    private static final String PROPERTYCLASS = "PROPERTYCLASS";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String TYPE = "TYPE";

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    private static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    private static final UUID CASE_2 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Court court = new Court();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setPropertyClass(PROPERTYCLASS);

        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(TYPE);
        documentProperties.setDescription(DESCRIPTION);

        Submission submissionWithParentApplication = Submission
                .builder()
                .modelPackage(TestHelpers.createPackage(court, Arrays.asList(documentProperties)))
                .create();

        Mockito.when(submissionStoreMock.getByKey(CASE_1)).thenReturn(Optional.of(submissionWithParentApplication));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock);
    }

    @Test
    @DisplayName("200: pass id and get values")
    public void withCorrectIDReturnResult() {
        ResponseEntity<ModelPackage> actual = sut.getSubmissionPackage(CASE_1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(DIVISION, actual.getBody().getCourt().getDivision());
        Assertions.assertEquals(FILENUMBER, actual.getBody().getCourt().getFileNumber());
        Assertions.assertEquals(LEVEL, actual.getBody().getCourt().getLevel());
        Assertions.assertEquals(LOCATION, actual.getBody().getCourt().getLocation());
        Assertions.assertEquals(PARTICIPATIONCLASS, actual.getBody().getCourt().getParticipatingClass());
        Assertions.assertEquals(PROPERTYCLASS, actual.getBody().getCourt().getPropertyClass());
        Assertions.assertEquals(TYPE, actual.getBody().getDocuments().get(0).getType());
        Assertions.assertEquals(DESCRIPTION, actual.getBody().getDocuments().get(0).getDescription());

    }
    @Test
    @DisplayName("404: with incorrect id return 404")
    public void withInCorrectIDReturnNotFound() {
        ResponseEntity<ModelPackage> actual = sut.getSubmissionPackage(CASE_2);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
}
