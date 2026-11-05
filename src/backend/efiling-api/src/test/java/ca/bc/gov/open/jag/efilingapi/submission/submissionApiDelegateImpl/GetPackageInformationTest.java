package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmissionFilingPackage;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;
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

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @Mock
    private Jwt jwtMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submissionWithParentApplication = Submission
                .builder()
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), createDocumentListWithNulls(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
                .create();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submissionWithParentApplication));

        Mockito.when(submissionServiceMock.isRushRequired(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
    }

    @Test
    @DisplayName("200: pass id and get values")
    public void withCorrectIDReturnResult() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<SubmissionFilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getBody().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getBody().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getBody().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getBody().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getBody().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getBody().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.TYPE, actual.getBody().getDocuments().get(0).getType());
        Assertions.assertTrue(actual.getBody().getDocuments().get(0).getRushRequired());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getDocuments().get(0).getDescription());
        Assertions.assertNull(actual.getBody().getDocuments().get(0).getIsAmendment());
        Assertions.assertNull(actual.getBody().getDocuments().get(0).getIsSupremeCourtScheduling());

    }

    @Test
    @DisplayName("404: with incorrect id return 404")
    public void withInCorrectIDReturnNotFound() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<SubmissionFilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    @DisplayName("404: with no universal id should throw InvalidUniversalException")
    public void withInCorrectIDThrowInvalidUniversalException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());

    }

    private List<Document> createDocumentListWithNulls() {
        return Collections.singletonList(Document.builder()
                .description(TestHelpers.DESCRIPTION)
                .statutoryFeeAmount(BigDecimal.TEN)
                .name("random.txt")
                .type(TestHelpers.TYPE)
                .subType(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD)
                .mimeType("application/txt")
                .isSupremeCourtScheduling(null)
                .isAmendment(null)
                .data(new Object()).create());
    }
}
