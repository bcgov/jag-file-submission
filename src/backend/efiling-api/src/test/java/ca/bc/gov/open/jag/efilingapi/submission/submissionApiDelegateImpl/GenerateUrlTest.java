package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Valid;
import java.util.*;

import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.INVALIDUNIVERSAL;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GenerateUrlTest {

    private static final String TYPE = "type";
    private static final String DISPLAYNAME = "DISPLAYNAME";
    private static final String CODE = "CODE";

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
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;


    @Mock
    private ClamAvService clamAvServiceMock;
    private UUID transactionId = UUID.randomUUID();


    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submission = Submission.builder().id(TestHelpers.CASE_1).transactionId(transactionId).expiryDate(10).create();

        Mockito
                .when(submissionServiceMock.generateFromRequest(
                        ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)),
                        Mockito.any()))
                .thenReturn(submission);

        Mockito.doThrow(new CSOHasMultipleAccountException("CSOHasMultipleAccountException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)),
                Mockito.any());

        Mockito.doThrow(new InvalidAccountStateException("InvalidAccountStateException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_3)),
                Mockito.any());

        Mockito.doThrow(new StoreException("StoreException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_4)),
                Mockito.any());

        Mockito.doThrow(new EfilingDocumentServiceException("EfilingDocumentServiceException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_5)),
                Mockito.any());

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper);

    }


    @Test
    @DisplayName("200: Valid request should generate a URL")
    public void withValidRequestShouldGenerateUrl() {

        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.CSO_APPLICATION_CODE, CODE);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        generateUrlRequest.setClientAppName(DISPLAYNAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(transactionId, UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_1, generateUrlRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("http://localhost?submissionId=" + TestHelpers.CASE_1.toString() + "&transactionId="  + transactionId, actual.getBody().getEfilingUrl());
        Assertions.assertNotNull(actual.getBody().getExpiryDate());

    }


    @Test
    @DisplayName("400: when CSOHasMultipleAccountException should return Bad Request")
    public void whenCSOHasMultipleAccountExceptionShouldReturnBadRequest() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(DISPLAYNAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_2, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.ACCOUNTEXCEPTION.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.ACCOUNTEXCEPTION.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("403: when InvalidAccountStateException should return FORBIDDEN")
    public void whenInvalidAccountStateExceptionShouldReturnForbidden() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(DISPLAYNAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_3, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.INVALIDROLE.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.INVALIDROLE.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("500: when StoreException should return INTERNAL SERVER ERROR")
    public void whenStoreExceptionShouldReturnInternalServerError() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(DISPLAYNAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_4, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.CACHE_ERROR.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.CACHE_ERROR.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("500: when DocumentException should return INTERNAL SERVER ERROR")
    public void whenDocumentExceptionShouldReturnInternalServerError() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientAppName(DISPLAYNAME);
        generateUrlRequest.setNavigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), TestHelpers.CASE_5, generateUrlRequest);


        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("403: with invalid userId then return forbidden 403")
    public void withInvalidUserIDThenReturnForbidden() {


        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), "BADUUID", UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        Assertions.assertEquals(INVALIDUNIVERSAL.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(INVALIDUNIVERSAL.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }
}

