package ca.bc.gov.open.jag.efilingcsoclient.csoAccountServiceImpl;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcsoclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.CsoHelpers;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.AccountDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.ClientProfileMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Create Account Test Suite")
public class CreateAccountTest {

    public static final String UNIVERSAL_ID = "77da92db-0791-491e-8c58-1a969e67d2fe";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String EMAIL = "EMAIL";
    private static final String MIDDLE_NAME = "MIDDLENAME";
    private static final String ACCOUNT_PREFIX_TXT = "SA";
    private static final String ACCOUNT_STATUS_CD = "ACT";
    private static final Date DATE = new Date();
    private static final String CLIENT_PREFIX_TXT = "CS";
    private static final String CLIENT_STATUS_CD = "ACT";
    private static final String REGISTERED_CLIENT_ROLE_CD2 = "FILE";
    public static final String DOMAIN = "Courts";
    public static final String APPLICATION = "CSO";
    public static final String IDENTIFIER_TYPE = "CAP";
    public static final String BCEID = "BCEID";
    public static final String IDENTITY_PROVIDER = "TEST";

    @Mock
    AccountFacadeBean accountFacadeBeanMock;

    @Mock
    RoleRegistryPortType roleRegistryPortTypeMock;

    @Mock
    AccountDetailsMapper accountDetailsMapperMock;

    private CsoAccountServiceImpl sut;

    @BeforeAll
    public void setUp() throws NestedEjbException_Exception, DatatypeConfigurationException {

        MockitoAnnotations.openMocks(this);
        accountDetailsMapperMock = new AccountDetailsMapperImpl();
        Mockito.when(accountFacadeBeanMock.createAccount(any(), any(), any(), any(), any(), any(), any())).thenReturn(createClientProfile());

        RegisteredRole fileRole = new RegisteredRole();
        fileRole.setCode(REGISTERED_CLIENT_ROLE_CD2);
        UserRoles userRolesWithFileRole = new UserRoles();
        userRolesWithFileRole.getRoles().add(fileRole);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier(DOMAIN, APPLICATION, CsoHelpers.formatUserGuid(UNIVERSAL_ID), IDENTIFIER_TYPE)).thenReturn(userRolesWithFileRole);
        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, roleRegistryPortTypeMock, accountDetailsMapperMock, new ClientProfileMapperImpl());
    }

    @Test
    @DisplayName("No account details missing first name")
    public void withMissingFirstNameThrowIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder().create()));

    }

    @Test
    @DisplayName("No account details missing last name")
    public void withMissingLastNameRequestThrowIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder()
                .firstName(FIRST_NAME)
                .create()));

    }

    @Test
    @DisplayName("No account details missing email")
    public void withMissingEmalRequestThrowIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .create()));

    }

    @Test
    @DisplayName("No account details universal id")
    public void withMissingUniversalIdRequestThrowIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .create()));

    }

    @Test
    @DisplayName("No account details missing identity provider")
    public void withMissingIdentityProviderRequestThrowIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .middleName(MIDDLE_NAME)
                .universalId(UNIVERSAL_ID)
                .create()));

    }

    @Test
    @DisplayName("No account details invalid identity provider")
    public void withInvalidIdentityProviderRequestThrowIllegalArgument() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .middleName(MIDDLE_NAME)
                .universalId(UNIVERSAL_ID)
                .identityProvider(IDENTITY_PROVIDER)
                .create()));

    }

    @Test
    @DisplayName("Success account created ")
    public void withValidAccountRequestAccountCreated() {
        AccountDetails result = sut.createAccount(createAccountRequest());
        Assertions.assertEquals(UNIVERSAL_ID, result.getUniversalId());
        Assertions.assertTrue(result.isFileRolePresent());
        Assertions.assertEquals(BigDecimal.TEN, result.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, result.getClientId());
    }

    @Test
    @DisplayName("Facade throws exception")
    public void withValidValuesFacadeThrowsException() throws NestedEjbException_Exception {
        Mockito.when(accountFacadeBeanMock.createAccount(any(), any(), any(), any(), any(), any(), any())).thenThrow(NestedEjbException_Exception.class);
        Assertions.assertThrows(EfilingAccountServiceException.class, () -> sut.createAccount(createAccountRequest()));
    }

    private CreateAccountRequest createAccountRequest() {
        return CreateAccountRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .middleName(MIDDLE_NAME)
                .universalId(UNIVERSAL_ID)
                .identityProvider(BCEID)
                .create();
    }

    private ClientProfile createClientProfile() throws DatatypeConfigurationException {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setAccount(createAccountDetails());

        clientProfile.setClient(createAccountClientDetails());
        clientProfile.setAccountId(BigDecimal.TEN);
        clientProfile.setClientId(BigDecimal.TEN);
        return clientProfile;
    }
    private Account createAccountDetails() throws DatatypeConfigurationException {

        Account account = new Account();
        account.setAccountNm(String.format("%s %s", FIRST_NAME, LAST_NAME));
        account.setAccountPrefixTxt(ACCOUNT_PREFIX_TXT);
        account.setAccountStatusCd(ACCOUNT_STATUS_CD);
        account.setAuthenticatedAccountGuid(CsoHelpers.formatUserGuid(UNIVERSAL_ID));
        account.setEmailTxt(EMAIL);
        account.setEntDtm(CsoHelpers.date2XMLGregorian(DATE));
        account.setFeeExemptYnBoolean(false);
        account.setRegisteredCreditCardYnBoolean(false);
        account.setAccountManager(createAccountClientDetails());

        return account;
    }

    private Client createAccountClientDetails() throws DatatypeConfigurationException {
        Client client = new Client();

        client.setAuthenticatedClientGuid(CsoHelpers.formatUserGuid(UNIVERSAL_ID));
        client.setClientPrefixTxt(CLIENT_PREFIX_TXT);
        client.setClientStatusCd(CLIENT_STATUS_CD);
        client.setEntDtm(CsoHelpers.date2XMLGregorian(DATE));
        client.setGivenNm(FIRST_NAME);
        client.setMiddleNm(MIDDLE_NAME);
        client.setRegisteredCreditCardYnBoolean(false);
        client.setSurnameNm(LAST_NAME);

        return client;
    }
}
