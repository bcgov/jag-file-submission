package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingPaymentException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class SubmitTest {
    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private NavigationProperties navigationPropertiesMock;

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
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @Mock
    private AccessToken.Access resourceAccessMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        Submission submissionExists = Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito
                .doReturn(Optional.of(submissionExists))
                .when(submissionStoreMock).get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)));

        Submission submissionError = Submission
                .builder()
                .id(TestHelpers.CASE_2)
                .navigationUrls(TestHelpers.createNavigation(null, null, null))
                .create();

        Submission submissionPaymentError = Submission
                .builder()
                .id(TestHelpers.CASE_4)
                .navigationUrls(TestHelpers.createNavigation(null, null, null))
                .create();

        Mockito
                .doReturn(Optional.of(submissionError))
                .when(submissionStoreMock)
                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)));

        Mockito
                .doReturn(Optional.of(submissionPaymentError))
                .when(submissionStoreMock)
                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_4)));

        SubmitResponse result = new SubmitResponse();
        result.setPackageRef("packageref");

        Mockito
                .when(submissionServiceMock.createSubmission(Mockito.refEq(submissionExists), Mockito.any(), Mockito.any()))
                .thenReturn(result);

        Mockito.doThrow(EfilingSubmissionServiceException.class).when(submissionServiceMock).createSubmission(ArgumentMatchers.argThat(x -> x.getId().equals(TestHelpers.CASE_2)), Mockito.any(), Mockito.any());
        Mockito.doThrow(EfilingPaymentException.class).when(submissionServiceMock).createSubmission(ArgumentMatchers.argThat(x -> x.getId().equals(TestHelpers.CASE_4)), Mockito.any(), Mockito.any());

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationPropertiesMock, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator);

    }

    @Test
    @DisplayName("201: With valid request should return created and service id not early adopter")
    public void withUserHavingValidRequestShouldReturnCreated() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(resourceAccessMock.isUserInRole(ArgumentMatchers.eq("early-adopters"))).thenReturn(true);
        Mockito.when(tokenMock.getResourceAccess(ArgumentMatchers.eq(Keys.EFILING_API_NAME))).thenReturn(resourceAccessMock);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<SubmitResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, null);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals("packageref", actual.getBody().getPackageRef());

    }

    @Test
    @DisplayName("201: With valid request should return created and service id early adopter")
    public void withUserHavingValidRequestEarlyAdopterShouldReturnCreated() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<SubmitResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, null);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals("packageref", actual.getBody().getPackageRef());

    }

    @Test
    @DisplayName("500: with valid request but soap service throws an exception return 500")
    public void withErrorInServiceShouldReturnInternalServiceError() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_2, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertEquals("SUBMISSION_FAILURE", ((EfilingError)actual.getBody()).getError());
        assertEquals("Error while submitting filing package", ((EfilingError)actual.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: with valid request but bambora throws an exception return 400")
    public void withErrorInBamboraShouldReturnBadRequest() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_4, null);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("PAYMENT_FAILURE", ((EfilingError)actual.getBody()).getError());
        assertEquals("Error while making payment", ((EfilingError)actual.getBody()).getMessage());

    }

    @Test
    @DisplayName("404: with submission request that does not exist 404 should be returned")
    public void withSubmissionRequestThatDoesNotExist() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, null);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: with no universal id should throw InvalidUniversalException")
    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY,null);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, null));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }
}
