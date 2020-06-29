package ca.bc.gov.open.jag.efilingaccountclient;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistry;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CsoAccountServiceImplTest {

    private static final String USERGUIDNOROLE = "USERGUIDNOROLE";
    private static final String USERGUIDWITHFILEROLE = "USERGUIDWITHFILEROLE";

    CsoAccountServiceImpl sut;

    @Mock
    AccountFacade accountFacadeMock;

    @Mock
    AccountFacadeBean accountFacadeBeanMock;

    @Mock
    RoleRegistry roleRegistryMock;

    @Mock
    RoleRegistryPortType roleRegistryPortTypeMock;

    @Mock
    AccountDetailsMapper accountDetailsMapperMock;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        ClientProfile profile =  new ClientProfile();
        profile.setAccountId(BigDecimal.TEN);
        profile.setClientId(BigDecimal.TEN);
        List<ClientProfile> profiles = new ArrayList<ClientProfile>();
        profiles.add(profile);

        RegisteredRole fileRole = new RegisteredRole();
        fileRole.setCode("FILE");
        UserRoles userRolesWithFileRole = new UserRoles();
        userRolesWithFileRole.getRoles().add(fileRole);

        RegisteredRole notFileRole = new RegisteredRole();
        notFileRole.setCode("NOTFILE");
        UserRoles userRolesWithoutFileRole = new UserRoles();
        userRolesWithoutFileRole.getRoles().add(notFileRole);

        Mockito.when(accountFacadeMock.getAccountFacadeBeanPort()).thenReturn(accountFacadeBeanMock);
        Mockito.when(accountFacadeBeanMock.findProfiles(any())).thenReturn(profiles);
        Mockito.when(roleRegistryMock.getRoleRegistrySourceRoleRegistryWsProviderRoleRegistryPort()).thenReturn(roleRegistryPortTypeMock);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier("Courts", "CSO", USERGUIDWITHFILEROLE, "CAP")).thenReturn(userRolesWithFileRole);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier("Courts", "CSO", USERGUIDNOROLE, "CAP")).thenReturn(userRolesWithoutFileRole);

        AccountDetails csoUserDetailsWithRole = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "firstName", "lastName", "middleName", "email");
        Mockito.when(accountDetailsMapperMock.toCsoAccountDetails(Mockito.any(), Mockito.eq(true))).thenReturn(csoUserDetailsWithRole);

        AccountDetails csoUserDetailsWithoutRole = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, false, "firstName", "lastName", "middleName","email");
        Mockito.when(accountDetailsMapperMock.toCsoAccountDetails(Mockito.any(), Mockito.eq(false))).thenReturn(csoUserDetailsWithoutRole);


        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, roleRegistryPortTypeMock, accountDetailsMapperMock);

    }

    @DisplayName("getAccountDetails called with empty userGuid")
    @Test
    public void testWithEmptyUserGuid() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails("");
        Assertions.assertEquals(null, details);
    }

    @DisplayName("getAccountDetails called with userGuid with file role")
    @Test
    public void testWithFileRoleEnabled() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USERGUIDWITHFILEROLE);
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
        Assertions.assertEquals(true, details.isEfileRolePresent());
        Assertions.assertEquals("firstName", details.getFirstName());
        Assertions.assertEquals("lastName", details.getLastName());
        Assertions.assertEquals("email", details.getEmail());

    }

    @DisplayName("getAccountDetails called with a userGuid that does not have file role")
    @Test
    public void testWithFileRoleDisabled() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USERGUIDNOROLE);
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
        Assertions.assertEquals(false, details.isEfileRolePresent());
        Assertions.assertEquals("firstName", details.getFirstName());
        Assertions.assertEquals("lastName", details.getLastName());
        Assertions.assertEquals("email", details.getEmail());

    }
}
