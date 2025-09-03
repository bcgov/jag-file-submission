package ca.bc.gov.open.jag.efilingapi.account;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CsoAccountMapperImpl;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.CsoAccount;
import ca.bc.gov.open.jag.efilingapi.api.model.CsoAccountUpdateRequest;
import ca.bc.gov.open.jag.efilingapi.error.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
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
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.IDENTITY_PROVIDER_CLAIM_KEY;
import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CsoAccountApiDelegateImpl Test Suite")
public class CsoAccountApiDelegateImplTest {

    private static final String LAST_NAME = "lastName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String EMAIL = "email@email.com";
    private static final String FIRST_NAME = "firstName";
    private static final String INTERNAL_CLIENT_NUMBER = "123456";
    private static final String FAIL_INTERNAL_CLIENT_NUMBER = "23456";

    private CsoAccountApiDelegateImpl sut;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private Jwt jwtMock;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);


        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);

        AccountDetails accountDetails = AccountDetails.builder()
                .fileRolePresent(true)
                .accountId(BigDecimal.ONE)
                .clientId(BigDecimal.TEN)
                .cardRegistered(true)
                .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                .universalId(TestHelpers.CASE_1_STRING).create();

        Mockito
                .doReturn(accountDetails)
                .when(accountServiceMock)
                .createAccount(Mockito.eq(TestHelpers.CASE_1_STRING), Mockito.any(), Mockito.any());

        Mockito
                .doReturn(accountDetails)
                .when(accountServiceMock)
                .getCsoAccountDetails(Mockito.eq(TestHelpers.CASE_1_STRING));

        Mockito
                .doReturn(null)
                .when(accountServiceMock)
                .getCsoAccountDetails(Mockito.eq(TestHelpers.CASE_2_STRING));

        Mockito
                .doThrow(new EfilingAccountServiceException("random"))
                .when(accountServiceMock)
                .createAccount(Mockito.eq(TestHelpers.CASE_2_STRING), Mockito.any(), Mockito.any());


        Mockito.doNothing().when(accountServiceMock).updateClient(
                ArgumentMatchers.argThat(x -> x.getInternalClientNumber().equals(INTERNAL_CLIENT_NUMBER))
        );

        Mockito.doThrow(EfilingAccountServiceException.class).when(accountServiceMock).updateClient(
                ArgumentMatchers.argThat(x -> x.getInternalClientNumber().equals(FAIL_INTERNAL_CLIENT_NUMBER))
        );


        // Testing mapper in this test
        CsoAccountMapper csoAccountMapper = new CsoAccountMapperImpl();

        sut = new CsoAccountApiDelegateImpl(accountServiceMock, csoAccountMapper);
    }


    @Test
    @DisplayName("201: should return an account with cso")
    public void whenAccountCreatedShouldReturn201() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(jwtMock.getClaim(Mockito.eq(IDENTITY_PROVIDER_CLAIM_KEY))).thenReturn(TestHelpers.IDENTITY_PROVIDER);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CreateCsoAccountRequest request = new CreateCsoAccountRequest();
        request.setLastName(LAST_NAME);
        request.setMiddleName(MIDDLE_NAME);
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);
        ResponseEntity<CsoAccount> actual = sut.createAccount(TestHelpers.CASE_1, request);

        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Assertions.assertEquals("1", actual.getBody().getAccountId());
        Assertions.assertEquals("10", actual.getBody().getClientId());
        Assertions.assertEquals(true, actual.getBody().getFileRolePresent());
        Assertions.assertEquals(INTERNAL_CLIENT_NUMBER, actual.getBody().getInternalClientNumber());

    }

    @Test
    @DisplayName("403: when universal id is missing should throw MissingUniversalIdException")
    public void createAccountWithUserNotHavingUniversalIdShouldThrowMissingUniversalIdException() {


        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CreateCsoAccountRequest request = new CreateCsoAccountRequest();
        request.setLastName(LAST_NAME);
        request.setMiddleName(MIDDLE_NAME);
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);

        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.createAccount(TestHelpers.CASE_1, request));
        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
    }


    @Test
    @DisplayName("500: when exception should throw CreateAccountException")
    public void createAccountWhenEfilingAccountServiceExceptionShouldThrowCreateAccountException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(jwtMock.getClaim(Mockito.eq(IDENTITY_PROVIDER_CLAIM_KEY))).thenReturn(TestHelpers.IDENTITY_PROVIDER);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CreateCsoAccountRequest request = new CreateCsoAccountRequest();
        request.setLastName(LAST_NAME);
        request.setMiddleName(MIDDLE_NAME);
        request.setEmail(EMAIL);
        request.setFirstName(FIRST_NAME);

        CreateAccountException exception = Assertions.assertThrows(CreateAccountException.class, () -> sut.createAccount(TestHelpers.CASE_2, request));
        Assertions.assertEquals(ErrorCode.CREATE_ACCOUNT_EXCEPTION.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: when creating account identity provider is missing should throw MissingIdentityProviderException")
    public void createAccountWithUserNotHavingIdentityProviderShouldThrowMissingIdentityProviderException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        MissingIdentityProviderException exception = Assertions.assertThrows(MissingIdentityProviderException.class, () -> sut.createAccount(TestHelpers.CASE_3, null));
        Assertions.assertEquals(ErrorCode.MISSING_IDENTITY_PROVIDER.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("200: should return a cso account")
    public void getAccountWithExistingAccountShouldReturnAccount() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(jwtMock.getClaim(Mockito.eq(IDENTITY_PROVIDER_CLAIM_KEY))).thenReturn(TestHelpers.IDENTITY_PROVIDER);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<CsoAccount> actual = sut.getCsoAccount(TestHelpers.CASE_1);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: with account not found should return not found")
    public void getAccountWithNoAccountShouldReturnNotFound() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_2.toString());
        Mockito.when(jwtMock.getClaim(Mockito.eq(IDENTITY_PROVIDER_CLAIM_KEY))).thenReturn(TestHelpers.IDENTITY_PROVIDER);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        ResponseEntity<CsoAccount> actual = sut.getCsoAccount(TestHelpers.CASE_1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: when getting account universal id is missing should return 403")
    public void getAccountWithUserNotHavingUniversalIdShouldReturn403() {

        ResponseEntity<CsoAccount> actual = sut.getCsoAccount(TestHelpers.CASE_3);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }

    @Test
    @DisplayName("200: With user having cso account and efiling role return submission details")
    public void updateAccountWithUserHavingCsoAccountShouldReturnUserDetailsAndAccount() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(jwtMock.getClaim(Mockito.eq(IDENTITY_PROVIDER_CLAIM_KEY))).thenReturn(TestHelpers.IDENTITY_PROVIDER);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CsoAccountUpdateRequest clientUpdateRequest = new CsoAccountUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);

        ResponseEntity<CsoAccount> actual = sut.updateCsoAccount(TestHelpers.CASE_1, clientUpdateRequest);
        assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: With user not having cso account should throw InvalidUniversalException")
    public void updateAccountWithUserHavingNoCsoAccountShouldThrowInvalidUniversalException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CsoAccountUpdateRequest clientUpdateRequest = new CsoAccountUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.updateCsoAccount(TestHelpers.CASE_2, clientUpdateRequest));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("500: with exception in soap service should throw UpdateClientException")
    public void updateAccountWithExceptionShouldThrowUpdateClientException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(TestHelpers.CASE_1.toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CsoAccountUpdateRequest clientUpdateRequest = new CsoAccountUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(FAIL_INTERNAL_CLIENT_NUMBER);

        UpdateClientException exception = Assertions.assertThrows(UpdateClientException.class, () -> sut.updateCsoAccount(TestHelpers.CASE_2, clientUpdateRequest));
        Assertions.assertEquals(ErrorCode.UPDATE_CLIENT_EXCEPTION.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: with no universal id should throw InvalidUniversalException")
    public void updateAccountWithUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        CsoAccountUpdateRequest clientUpdateRequest = new CsoAccountUpdateRequest();
        clientUpdateRequest.setInternalClientNumber(INTERNAL_CLIENT_NUMBER);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.updateCsoAccount(TestHelpers.CASE_2, clientUpdateRequest));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }


}
