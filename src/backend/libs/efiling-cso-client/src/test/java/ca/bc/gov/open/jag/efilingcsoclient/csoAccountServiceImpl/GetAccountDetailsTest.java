package ca.bc.gov.open.jag.efilingcsoclient.csoAccountServiceImpl;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistry;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcsoclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.CsoHelpers;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.AccountDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.ClientProfileMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Get Account Details Test Suite")
public class GetAccountDetailsTest {

    private static final String USER_GUID_NO_ROLE = UUID.randomUUID().toString();
    private static final String USER_GUID_WITH_FILE_ROLE = UUID.randomUUID().toString();
    private static final String USER_GUID_WITH_NO_CSO = UUID.randomUUID().toString();
    private static final String USER_GUID_WITH_EJB_EXCEPTION = UUID.randomUUID().toString();
    private static final String USER_GUID_WITH_MULTI_PROFILE = UUID.randomUUID().toString();
    public static final String DOMAIN = "Courts";
    public static final String APPLICATION = "CSO";
    public static final String IDENTIFIER_TYPE = "CAP";
    private static final String INTERNAL_CLIENT_NUMBER = "123";

    CsoAccountServiceImpl sut;

    @Mock
    AccountFacade accountFacadeMock;

    @Mock
    AccountFacadeBean accountFacadeBeanMock;

    @Mock
    RoleRegistry roleRegistryMock;

    @Mock
    RoleRegistryPortType roleRegistryPortTypeMock;

    AccountDetailsMapper accountDetailsMapper;

    @BeforeEach
    public void beforeEach() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);
        initAccountFacadeMocks();
        initRoleRegistryMocks();

        accountDetailsMapper = new AccountDetailsMapperImpl();

        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, roleRegistryPortTypeMock, accountDetailsMapper, new ClientProfileMapperImpl());

    }

    private void initAccountFacadeMocks() throws NestedEjbException_Exception {

        Mockito.when(accountFacadeMock.getAccountFacadeBeanPort()).thenReturn(accountFacadeBeanMock);

        ClientProfile profile =  new ClientProfile();
        profile.setAccountId(BigDecimal.TEN);
        profile.setClientId(BigDecimal.TEN);
        Client client = new Client();
        client.setInternalClientNo(INTERNAL_CLIENT_NUMBER);
        profile.setClient(client);

        List<ClientProfile> profiles = new ArrayList<ClientProfile>();
        profiles.add(profile);

        Mockito.when(accountFacadeBeanMock.findProfiles(Mockito.eq(CsoHelpers.formatUserGuid(USER_GUID_NO_ROLE)))).thenReturn(profiles);
        Mockito.when(accountFacadeBeanMock.findProfiles(Mockito.eq(CsoHelpers.formatUserGuid(USER_GUID_WITH_FILE_ROLE)))).thenReturn(profiles);

        List<ClientProfile> emptyProfiles = new ArrayList<ClientProfile>();
        Mockito.when(accountFacadeBeanMock.findProfiles(Mockito.eq(CsoHelpers.formatUserGuid(USER_GUID_WITH_NO_CSO)))).thenReturn(emptyProfiles);
        Mockito.when(accountFacadeBeanMock.findProfiles(Mockito.eq(CsoHelpers.formatUserGuid(USER_GUID_WITH_EJB_EXCEPTION)))).thenThrow(new NestedEjbException_Exception("random"));

        List<ClientProfile> multiProfiles = new ArrayList<>();
        multiProfiles.add(profile);
        multiProfiles.add(profile);
        Mockito.when(accountFacadeBeanMock.findProfiles(Mockito.eq(CsoHelpers.formatUserGuid(USER_GUID_WITH_MULTI_PROFILE)))).thenReturn(multiProfiles);

    }

    private void initRoleRegistryMocks() {

        RegisteredRole fileRole = new RegisteredRole();
        fileRole.setCode("FILE");
        UserRoles userRolesWithFileRole = new UserRoles();
        userRolesWithFileRole.getRoles().add(fileRole);

        RegisteredRole notFileRole = new RegisteredRole();
        notFileRole.setCode("NOTFILE");
        UserRoles userRolesWithoutFileRole = new UserRoles();
        userRolesWithoutFileRole.getRoles().add(notFileRole);

        Mockito.when(roleRegistryMock.getRoleRegistrySourceRoleRegistryWsProviderRoleRegistryPort()).thenReturn(roleRegistryPortTypeMock);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier(DOMAIN, APPLICATION, CsoHelpers.formatUserGuid(USER_GUID_WITH_FILE_ROLE), IDENTIFIER_TYPE)).thenReturn(userRolesWithFileRole);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier(DOMAIN, APPLICATION, CsoHelpers.formatUserGuid(USER_GUID_NO_ROLE), IDENTIFIER_TYPE)).thenReturn(userRolesWithoutFileRole);

    }

    @DisplayName("OK: getAccountDetails called with userGuid with file role")
    @Test
    public void testWithFileRoleEnabled() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USER_GUID_WITH_FILE_ROLE);
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
        Assertions.assertEquals(INTERNAL_CLIENT_NUMBER, details.getInternalClientNumber());
        Assertions.assertEquals(true, details.isFileRolePresent());

    }

    @DisplayName("OK: getAccountDetails called with a userGuid that does not have file role")
    @Test
    public void testWithFileRoleDisabled() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USER_GUID_NO_ROLE);
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
        Assertions.assertEquals(INTERNAL_CLIENT_NUMBER, details.getInternalClientNumber());
        Assertions.assertEquals(false, details.isFileRolePresent());

    }

    @DisplayName("OK: getAccountDetails called with a userGuid that does not have cso account should return null")
    @Test
    public void withNoCsoAccountShouldReturnNull() throws NestedEjbException_Exception {

        AccountDetails actual = sut.getAccountDetails(USER_GUID_WITH_NO_CSO);
        Assertions.assertNull(actual);
    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingAccountServiceException")
    @Test
    public void withNestedEjbExceptionExceptionShouldThrowEfilingAccountServiceException() {


        EfilingAccountServiceException actual = Assertions.assertThrows(EfilingAccountServiceException.class, () -> {
            sut.getAccountDetails(USER_GUID_WITH_EJB_EXCEPTION);
        });

        Assertions.assertEquals(NestedEjbException_Exception.class, actual.getCause().getClass());
        Assertions.assertEquals("Exception while fetching account details", actual.getMessage());

    }

    @DisplayName("Exception: with multiprofiles should throw CSOHasMultipleAccountException")
    @Test
    public void withMultiProflieShouldThrowCSOHasMultipleAccountException() {


        CSOHasMultipleAccountException actual = Assertions.assertThrows(CSOHasMultipleAccountException.class, () -> {
            sut.getAccountDetails(USER_GUID_WITH_MULTI_PROFILE);
        });

        Assertions.assertEquals("Client 10 has multiple CSO profiles", actual.getMessage());

    }
}
