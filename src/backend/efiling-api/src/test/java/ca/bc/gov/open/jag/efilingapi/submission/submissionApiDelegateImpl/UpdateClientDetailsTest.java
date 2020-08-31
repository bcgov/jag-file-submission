package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.ClientUpdateRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UpdateClientDetails")
public class UpdateClientDetailsTest {

    private static final String FAIL_INTERNAL_CLIENT_NUMBER = "1234";
    private static final String INTERNAL_CLIENT_NUMBER = "123";

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
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;


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
        Submission submissionWithCsoAccount = Submission
                .builder()
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .clientApplication(TestHelpers.createClientApplication(TestHelpers.DESCRIPTION, TestHelpers.TYPE))
                .accountDetails(AccountDetails.builder()
                        .clientId(BigDecimal.TEN)
                        .create())
                .create();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)))).thenReturn(Optional.of(submissionWithCsoAccount));

        Mockito.doNothing().when(accountServiceMock).updateClient(
                ArgumentMatchers.argThat(x -> x.getInternalClientNumber().equals(INTERNAL_CLIENT_NUMBER))
        );

        Mockito.doThrow(EfilingAccountServiceException.class).when(accountServiceMock).updateClient(
                ArgumentMatchers.argThat(x -> x.getInternalClientNumber().equals(FAIL_INTERNAL_CLIENT_NUMBER))
        );

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, null, filingPackageMapper);

    }

    @Test
    @DisplayName("404: With null redis storage response return NotFound")
    public void withNullRedisStorageResponseReturnNotFound() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<Object> actual = sut.updateClientDetails(UUID.randomUUID(), TestHelpers.CASE_1, null);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("200: With user having cso account and efiling role return submission details")
    public void withUserHavingCsoAccountShouldReturnUserDetailsAndAccount() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);

        ResponseEntity<Object> actual = sut.updateClientDetails( TestHelpers.CASE_2, TestHelpers.CASE_2, clientUpdateRequest);
        assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("500: with exception in soap service throw 500")
    public void withExceptionShouldReturn500() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(FAIL_INTERNAL_CLIENT_NUMBER);

        ResponseEntity actual = sut.updateClientDetails(UUID.randomUUID(), TestHelpers.CASE_2, clientUpdateRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertEquals(ErrorResponse.UPDATE_CLIENT_EXCEPTION.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        assertEquals(ErrorResponse.UPDATE_CLIENT_EXCEPTION.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());


    }

    @Test
    @DisplayName("403: with no universal id is forbidden")
    public void withUserNotHavingUniversalIdShouldReturn403() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY,null);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ClientUpdateRequest clientUpdateRequest = new ClientUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(FAIL_INTERNAL_CLIENT_NUMBER);

        ResponseEntity actual = sut.updateClientDetails(UUID.randomUUID(), TestHelpers.CASE_2, clientUpdateRequest);
        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }

}
