package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtBase;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.*;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GenerateUrlTest {

    private static final String CODE = "CODE";
    private static final String CLIENT_APP_NAME = "clientAppName";
    private static final String USER_WITH_CSO_ACCOUNT = "1593769b-ac4b-43d9-9e81-38877eefcca5";
    private static final String USER_WITH_NO_CSO_ACCOUNT = "1593769b-ac4b-43d9-9e81-38877eefcca6";

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
    private Jwt jwtMock;

    @Mock
    private ClamAvService clamAvServiceMock;
    private UUID transactionId = UUID.randomUUID();

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidatorMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.setContext(securityContextMock);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submission = Submission.builder().id(TestHelpers.CASE_1).transactionId(transactionId).expiryDate(10).create();

        Mockito.when(accountServiceMock.getCsoAccountDetails(Mockito.eq(USER_WITH_CSO_ACCOUNT))).thenReturn(TestHelpers.createCSOAccountDetails(true));
        Mockito.when(accountServiceMock.getCsoAccountDetails(Mockito.eq(USER_WITH_NO_CSO_ACCOUNT))).thenReturn(null);

        Mockito
                .when(submissionServiceMock.generateFromRequest(
                        Mockito.any(),
                        ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)),
                        Mockito.any()))
                .thenReturn(submission);

        Mockito.doThrow(new CSOHasMultipleAccountException("CSOHasMultipleAccountException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)),
                Mockito.any());

        Mockito.doThrow(new InvalidAccountStateException("InvalidAccountStateException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_3)),
                Mockito.any());

        Mockito.doThrow(new StoreException("StoreException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_4)),
                Mockito.any());

        Mockito.doThrow(new EfilingDocumentServiceException("EfilingDocumentServiceException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_5)),
                Mockito.any());

        Notification notification = new Notification();
        Mockito.doReturn(notification).when(generateUrlRequestValidatorMock)
                .validate(
                        ArgumentMatchers.argThat(x -> x.getFilingPackage().getCourt().getLocation().equals("valid")),
                        Mockito.anyString(), Mockito.anyString());

        Notification invalidNotification = new Notification();
        invalidNotification.addError("a random error");
        Mockito.doReturn(invalidNotification).when(generateUrlRequestValidatorMock).validate(
                ArgumentMatchers.argThat(x -> x.getFilingPackage().getCourt().getLocation().equals("invalid")),
                Mockito.anyString(), Mockito.anyString());

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidatorMock, null);

        Mockito.when(jwtMock.getClaim(Mockito.eq(Keys.CSO_APPLICATION_CLAIM_KEY))).thenReturn(CODE);

    }


    @Test
    @DisplayName("200: Valid request should generate a URL")
    public void withValidRequestShouldGenerateUrl() {

        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(transactionId, USER_WITH_CSO_ACCOUNT.toString().replace("-", ""), TestHelpers.CASE_1, generateUrlRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("http://localhost?submissionId=" + TestHelpers.CASE_1.toString() + "&transactionId="  + transactionId, actual.getBody().getEfilingUrl());
        Assertions.assertNotNull(actual.getBody().getExpiryDate());

    }


    @Test
    @DisplayName("200: with user having no CSO account should return a valid url")
    public void withUserHavingNoCSOAccountShouldReturnValidUrl() {

        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(transactionId, USER_WITH_NO_CSO_ACCOUNT.toString().replace("-", ""), TestHelpers.CASE_1, generateUrlRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("http://localhost?submissionId=" + TestHelpers.CASE_1.toString() + "&transactionId="  + transactionId, actual.getBody().getEfilingUrl());
        Assertions.assertNotNull(actual.getBody().getExpiryDate());

    }

    @Test
    @DisplayName("400: with initialPackage validation failure should throw InvalidInitialSubmissionPayloadException")
    public void whenInitialPackageValidationFailureShouldThrowInvalidInitialSubmissionPayloadException() {

        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("invalid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        InvalidInitialSubmissionPayloadException exception = Assertions.assertThrows(InvalidInitialSubmissionPayloadException.class, () -> sut.generateUrl(transactionId, USER_WITH_NO_CSO_ACCOUNT.toString().replace("-", ""), TestHelpers.CASE_1, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.INVALID_INITIAL_SUBMISSION_PAYLOAD.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("400: when CSOHasMultipleAccountException should throw AccountException")
    public void whenCSOHasMultipleAccountExceptionShouldThrowAccountException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        AccountException exception = Assertions.assertThrows(AccountException.class, () -> sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_2, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.ACCOUNTEXCEPTION.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: when InvalidAccountStateException should throw InvalidRoleException")
    public void whenInvalidAccountStateExceptionShouldThrowInvalidRoleException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_3, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.INVALIDROLE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: when EFileRole not present should throw InvalidRoleException")
    public void whenEFileRoleNotPresentShouldThrowInvalidRoleException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        Mockito.when(accountServiceMock.getCsoAccountDetails(any())).thenReturn(TestHelpers.createCSOAccountDetails(false));

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        InvalidRoleException exception = Assertions.assertThrows(InvalidRoleException.class, () -> sut.generateUrl(transactionId, UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_1, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.INVALIDROLE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: when Application Code is not present should throw MissingApplicationCodeException")
    public void whenApplicationCodeNotPresentShouldThrowMissingApplicationCodeException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        Mockito.when(jwtMock.getClaim(Mockito.eq(Keys.CSO_APPLICATION_CLAIM_KEY))).thenReturn(null);

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        MissingApplicationCodeException exception = Assertions.assertThrows(MissingApplicationCodeException.class, () -> sut.generateUrl(transactionId, UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_1, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.MISSING_APPLICATION_CODE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("500: when StoreException should throw CacheException")
    public void whenStoreExceptionShouldThrowCacheException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        CacheException exception = Assertions.assertThrows(CacheException.class, () -> sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_4, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.CACHE_ERROR.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("500: when DocumentException should throw DocumentTypeException")
    public void whenDocumentExceptionShouldThrowDocumentTypeException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        DocumentTypeException exception = Assertions.assertThrows(DocumentTypeException.class, () -> sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_5, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.DOCUMENT_TYPE_ERROR.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: with no user id it should throw InvalidUniversalException")
    public void withNoUserIdShouldThrowInvalidUniversalException() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));
        InitialPackage initialPackage = new InitialPackage();
        CourtBase court = new CourtBase();
        court.setLocation("valid");
        initialPackage.setCourt(court);
        generateUrlRequest.setFilingPackage(initialPackage);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.generateUrl(UUID.randomUUID(), "  ", TestHelpers.CASE_5, generateUrlRequest));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }
}
