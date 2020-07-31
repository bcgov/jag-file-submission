package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
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

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private AccountService accountServiceMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submissionWithParentApplication = Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_1), Mockito.any())).thenReturn(Optional.of(submissionWithParentApplication));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock);
    }

    @Test
    @DisplayName("200: pass id and get values")
    public void withCorrectIDReturnResult() {
        ResponseEntity<FilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getBody().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getBody().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getBody().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getBody().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getBody().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getBody().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.TYPE, actual.getBody().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getDocuments().get(0).getDescription());

    }
    @Test
    @DisplayName("404: with incorrect id return 404")
    public void withInCorrectIDReturnNotFound() {
        ResponseEntity<FilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
}
