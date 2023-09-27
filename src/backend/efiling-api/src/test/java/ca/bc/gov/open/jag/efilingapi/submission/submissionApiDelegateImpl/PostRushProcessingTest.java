package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;
import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.RushProcessingMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import org.joda.time.DateTime;
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

import java.util.*;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRushProcessingTest {

    private static final String FILE_PDF = "file.pdf";
    private static final String COUNTRY = "COUNTRY";
    private static final String COUNTRY_CODE = "CD";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String ORGANIZATION = "ORGANIZATION";
    private static final String REASON = "REASON";
    private static final String PHONE_NUMBER = "1231231234";
    private static final String DATE = "2001-11-26T12:00:00Z";
    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private NavigationProperties navigationProperties;

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
    private Jwt jwtMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, new RushProcessingMapperImpl());

    }

    @Test
    @DisplayName("201")
    public void withValidParamtersReturnDocumentProperties() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        Submission submission = TestHelpers.buildSubmission();

        Mockito.when(submissionStoreMock.get(Mockito.any())).thenReturn(Optional.of(submission));

        Rush rush = new Rush();
        rush.setCountry(COUNTRY);
        rush.setCourtDate(DATE);
        rush.setCountryCode(COUNTRY_CODE);
        rush.setFirstName(FIRST_NAME);
        rush.setLastName(LAST_NAME);
        rush.setOrganization(ORGANIZATION);
        rush.setPhoneNumber(PHONE_NUMBER);
        rush.setReason(REASON);
        RushDocument document = new RushDocument();
        document.setFileName(FILE_PDF);
        rush.setSupportingDocuments(Arrays.asList(document));

        ResponseEntity<Void> actual = sut.postRushProcessing(TestHelpers.CASE_1, UUID.randomUUID(), rush);

        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());

    }

    @Test
    @DisplayName("404")
    public void withNoSubmissionReturnNotFound() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity actual = sut.postRushProcessing(TestHelpers.CASE_2, UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }


    @Test
    @DisplayName("403: with no universal id should throw InvalidUniversalException")
    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.postRushProcessing(TestHelpers.CASE_2, UUID.randomUUID(), null));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());

    }

}
