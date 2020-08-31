package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Document;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

    @Mock
    private ClamAvService clamAvServiceMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submissionWithParentApplication = Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), createDocumentListWithNulls(), TestHelpers.createPartyList()))
                .create();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submissionWithParentApplication));

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper);
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
        Assertions.assertNull(actual.getBody().getDocuments().get(0).getIsAmendment());
        Assertions.assertNull(actual.getBody().getDocuments().get(0).getIsSupremeCourtScheduling());

    }
    @Test
    @DisplayName("404: with incorrect id return 404")
    public void withInCorrectIDReturnNotFound() {
        ResponseEntity<FilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }
    private List<Document> createDocumentListWithNulls() {
        return Arrays.asList(ca.bc.gov.open.jag.efilingapi.submission.models.Document.builder()
                .description(TestHelpers.DESCRIPTION)
                .statutoryFeeAmount(BigDecimal.TEN)
                .name("random.txt")
                .type(TestHelpers.TYPE)
                .subType(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD)
                .mimeType("application/txt")
                .isSupremeCourtScheduling(null)
                .isAmendment(null)
                .data(new JsonObject()).create());
    }
}
