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

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    private static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        ParentApplication parentApplication = new ParentApplication();
        parentApplication.setApplicationType("ApplType");
        parentApplication.setCourtLocation("Kelwona law courts");
        parentApplication.setCourtLevel("P");
        parentApplication.setCourtDivision("R");
        parentApplication.setCourtClass("E");
        parentApplication.setParticipationClass("ParticipationClass");
        parentApplication.setIndigenousStatus("IndigenousStatus");
        parentApplication.setDocumentType("POR");
        parentApplication.setCourtFileNumber("123");

        Submission submissionWithParentApplication = Submission
                .builder()
                .parentApplication(parentApplication)
                .create();

        Mockito.when(submissionStoreMock.getByKey(CASE_1)).thenReturn(Optional.of(submissionWithParentApplication));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock);
    }

    @Test
    @DisplayName("200: pass id and get hardcoded values")
    public void withNullRedisStorageResponseReturnNotFound() {
        ResponseEntity<GetPacakageInformationResponse> actual = sut.getPackageInformation(CASE_1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals("ApplType", actual.getBody().getParentApplication().getApplicationType());
        assertEquals("Kelwona law courts", actual.getBody().getParentApplication().getCourtLocation());
        assertEquals("P", actual.getBody().getParentApplication().getCourtLevel());
        assertEquals("R", actual.getBody().getParentApplication().getCourtDivision());
        assertEquals("E", actual.getBody().getParentApplication().getCourtClass());
        assertEquals("ParticipationClass", actual.getBody().getParentApplication().getParticipationClass());
        assertEquals("IndigenousStatus", actual.getBody().getParentApplication().getIndigenousStatus());
        assertEquals("POR", actual.getBody().getParentApplication().getDocumentType());
        assertEquals("123", actual.getBody().getParentApplication().getCourtFileNumber());
    }
}
