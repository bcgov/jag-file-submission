package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.Account;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.UserDetails;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CsoAccountApiDelegateImpl Test Suite")
public class CsoAccountApiDelegateImplTest {

    public static final String LAST_NAME = "lastName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String EMAIL = "email@email.com";
    public static final String FIRST_NAME = "firstName";
    private CsoAccountApiDelegateImpl sut;

    @Mock
    private EfilingAccountService efilingAccountServiceMock;

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
        Mockito
                .doReturn(AccountDetails.builder()
                        .fileRolePresent(true)
                        .accountId(BigDecimal.ONE)
                        .universalId(TestHelpers.CASE_1)
                        .lastName(LAST_NAME)
                        .firstName(FIRST_NAME)
                        .middleName(MIDDLE_NAME)
                        .email(EMAIL).create())
                .when(efilingAccountServiceMock)
                .createAccount(ArgumentMatchers.argThat(x -> x.getUniversalId().equals(TestHelpers.CASE_1)));

        Mockito
                .doThrow(new EfilingAccountServiceException("random"))
                .when(efilingAccountServiceMock)
                .createAccount(ArgumentMatchers.argThat(x -> x.getUniversalId().equals(TestHelpers.CASE_2)));

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        sut = new CsoAccountApiDelegateImpl(efilingAccountServiceMock);
    }


    @Test
    @DisplayName("201: should return an account with cso")
    public void whenAccountCreatedShouldReturn201() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(LAST_NAME);
        userDetails.setMiddleName(MIDDLE_NAME);
        userDetails.setEmail(EMAIL);
        userDetails.setFirstName(FIRST_NAME);
        Account account = new Account();
        account.setType(Account.TypeEnum.BCEID);
        account.setIdentifier(UUID.randomUUID().toString());
        userDetails.addAccountsItem(account);
        ResponseEntity<UserDetails> actual = sut.createAccount(TestHelpers.CASE_1, userDetails);

        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Assertions.assertEquals(TestHelpers.CASE_1, actual.getBody().getUniversalId());
        Assertions.assertEquals(LAST_NAME, actual.getBody().getLastName());
        Assertions.assertEquals(MIDDLE_NAME, actual.getBody().getMiddleName());
        Assertions.assertEquals(EMAIL, actual.getBody().getEmail());
        Assertions.assertEquals(FIRST_NAME, actual.getBody().getFirstName());

    }

    @Test
    @DisplayName("403: when universal id is missing should return 403")
    public void withUserNotHavingUniversalIdShouldReturn403() {

        Map<String, Object> otherClaims = new HashMap<>();
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(LAST_NAME);
        userDetails.setMiddleName(MIDDLE_NAME);
        userDetails.setEmail(EMAIL);
        userDetails.setFirstName(FIRST_NAME);
        Account account = new Account();
        account.setType(Account.TypeEnum.BCEID);
        account.setIdentifier(UUID.randomUUID().toString());
        userDetails.addAccountsItem(account);
        ResponseEntity actual = sut.createAccount(TestHelpers.CASE_1, userDetails);

        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        assertEquals("MISSING_UNIVERSAL_ID", ((EfilingError)actual.getBody()).getError());
        assertEquals("universal-id claim missing in jwt token.", ((EfilingError)actual.getBody()).getMessage());


    }


    @Test
    @DisplayName("500: when exception should return 500")
    public void whenEfilingAccountServiceExceptionShouldReturn500() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_2);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(LAST_NAME);
        userDetails.setMiddleName(MIDDLE_NAME);
        userDetails.setEmail(EMAIL);
        userDetails.setFirstName(FIRST_NAME);
        Account account = new Account();
        account.setType(Account.TypeEnum.BCEID);
        account.setIdentifier(UUID.randomUUID().toString());
        userDetails.addAccountsItem(account);
        ResponseEntity actual = sut.createAccount(TestHelpers.CASE_2, userDetails);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());

    }

}
