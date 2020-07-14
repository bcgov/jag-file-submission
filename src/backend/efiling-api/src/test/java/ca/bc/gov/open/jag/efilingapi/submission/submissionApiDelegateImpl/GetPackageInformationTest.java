package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.api.model.ClientApplication;
import ca.bc.gov.open.jag.efilingapi.api.model.GetPacakageInformationResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.ParentApplication;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GetPackageInformationTest {

    private static final String APPL_TYPE = "ApplType";
    private static final String COURT_LOCATION = "Kelwona law courts";
    private static final String COURT_LEVEL = "P";
    private static final String COURT_DIVISION = "R";
    private static final String COURT_CLASS = "E";
    private static final String PARTICIPATION_CLASS = "ParticipationClass";
    private static final String INDIGENOUS_STATUS = "IndigenousStatus";
    private static final String DOCUMENT_TYPE = "POR";
    private static final String COURT_FILE_NUMBER = "123";
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

        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setApplicationType(APPL_TYPE);
        clientApplication.setCourtLocation(COURT_LOCATION);
        clientApplication.setCourtLevel(COURT_LEVEL);
        clientApplication.setCourtDivision(COURT_DIVISION);
        clientApplication.setCourtClass(COURT_CLASS);
        clientApplication.setParticipationClass(PARTICIPATION_CLASS);
        clientApplication.setIndigenousStatus(INDIGENOUS_STATUS);
        clientApplication.setDocumentType(DOCUMENT_TYPE);
        clientApplication.setCourtFileNumber(COURT_FILE_NUMBER);

        Submission submissionWithParentApplication = Submission
                .builder()
                .clientApplication(clientApplication)
                .create();

        Mockito.when(submissionStoreMock.getByKey(CASE_1)).thenReturn(Optional.of(submissionWithParentApplication));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock);
    }

    @Test
    @DisplayName("200: pass id and get values")
    public void withCorrectIDReturnResult() {
        ResponseEntity<GetPacakageInformationResponse> actual = sut.getSubmissionPackage(CASE_1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(APPL_TYPE, actual.getBody().getClientApplication().getApplicationType());
        assertEquals(COURT_LOCATION, actual.getBody().getClientApplication().getCourtLocation());
        assertEquals(COURT_LEVEL, actual.getBody().getClientApplication().getCourtLevel());
        assertEquals(COURT_DIVISION, actual.getBody().getClientApplication().getCourtDivision());
        assertEquals(COURT_CLASS, actual.getBody().getClientApplication().getCourtClass());
        assertEquals(PARTICIPATION_CLASS, actual.getBody().getClientApplication().getParticipationClass());
        assertEquals(INDIGENOUS_STATUS, actual.getBody().getClientApplication().getIndigenousStatus());
        assertEquals(DOCUMENT_TYPE, actual.getBody().getClientApplication().getDocumentType());
        assertEquals(COURT_FILE_NUMBER, actual.getBody().getClientApplication().getCourtFileNumber());
    }
    @Test
    @DisplayName("404: with incorrect id return 404")
    public void withInCorrectIDReturnNotFound() {
        ResponseEntity<GetPacakageInformationResponse> actual = sut.getSubmissionPackage(CASE_2);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
}
