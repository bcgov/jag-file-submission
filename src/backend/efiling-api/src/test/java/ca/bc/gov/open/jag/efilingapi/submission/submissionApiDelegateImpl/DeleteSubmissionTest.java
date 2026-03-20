package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
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
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;
import static ca.bc.gov.open.jag.efilingapi.TestHelpers.createDocumentList;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteSubmissionTest {

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private AccountService accountServiceMock;

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

        Mockito.doNothing().when(documentStoreMock).evict(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(submissionStoreMock).evict(Mockito.any());

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        Submission submission = Submission
                .builder()
                .universalId(UUID.randomUUID().toString())
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
                .create();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));


        NavigationProperties navigationProperties = new NavigationProperties();
        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock, null, filingPackageMapper, generateUrlRequestValidator, null);

    }

    @Test
    @DisplayName("200: should delete from submission")
    public void withSubmissionIdAndTransactionIdShouldDeleteSubmission() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity actual = sut.deleteSubmission(TestHelpers.CASE_1, UUID.randomUUID());

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: without submission should return not found")
    public void withoutSubmissionShouldDeleteSubmission() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity actual = sut.deleteSubmission(TestHelpers.CASE_2, UUID.randomUUID());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: with no universal id should throw InvalidUniversalException")
    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {


        BigDecimal test = new BigDecimal(100000000);

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.deleteSubmission(TestHelpers.CASE_2, UUID.randomUUID()));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());

    }


}
