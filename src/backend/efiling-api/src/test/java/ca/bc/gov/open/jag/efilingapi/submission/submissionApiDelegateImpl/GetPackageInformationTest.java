package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.GetPacakageInformationResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.GetSubmissionResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.ParentApplication;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GetPackageInformationTest {

    private static final String APPL_TYPE = "ApplType";
    private static final String KELWONA_LAW_COURTS = "Kelwona law courts";
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

        ParentApplication parentApplication = new ParentApplication();
        parentApplication.setApplicationType(APPL_TYPE);
        parentApplication.setCourtLocation(KELWONA_LAW_COURTS);
        parentApplication.setCourtLevel(COURT_LEVEL);
        parentApplication.setCourtDivision(COURT_DIVISION);
        parentApplication.setCourtClass(COURT_CLASS);
        parentApplication.setParticipationClass(PARTICIPATION_CLASS);
        parentApplication.setIndigenousStatus(INDIGENOUS_STATUS);
        parentApplication.setDocumentType(DOCUMENT_TYPE);
        parentApplication.setCourtFileNumber(COURT_FILE_NUMBER);

        Submission submissionWithParentApplication = Submission
                .builder()
                .parentApplication(parentApplication)
                .create();

        Mockito.when(submissionStoreMock.getByKey(CASE_1)).thenReturn(Optional.of(submissionWithParentApplication));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock);
    }

    @Test
    @DisplayName("200: pass id and get values")
    public void withCorrectIDReturnResult() {
        ResponseEntity<GetPacakageInformationResponse> actual = sut.getPackageInformation(CASE_1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(APPL_TYPE, actual.getBody().getParentApplication().getApplicationType());
        assertEquals(KELWONA_LAW_COURTS, actual.getBody().getParentApplication().getCourtLocation());
        assertEquals(COURT_LEVEL, actual.getBody().getParentApplication().getCourtLevel());
        assertEquals(COURT_DIVISION, actual.getBody().getParentApplication().getCourtDivision());
        assertEquals(COURT_CLASS, actual.getBody().getParentApplication().getCourtClass());
        assertEquals(PARTICIPATION_CLASS, actual.getBody().getParentApplication().getParticipationClass());
        assertEquals(INDIGENOUS_STATUS, actual.getBody().getParentApplication().getIndigenousStatus());
        assertEquals(DOCUMENT_TYPE, actual.getBody().getParentApplication().getDocumentType());
        assertEquals(COURT_FILE_NUMBER, actual.getBody().getParentApplication().getCourtFileNumber());
    }
    @Test
    @DisplayName("404: with incorrect id return 404")
    public void withInCorrectIDReturnNotFound() {
        ResponseEntity<GetPacakageInformationResponse> actual = sut.getPackageInformation(CASE_2);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
}
