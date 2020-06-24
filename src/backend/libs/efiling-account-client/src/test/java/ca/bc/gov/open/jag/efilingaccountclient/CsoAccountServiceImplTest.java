package ca.bc.gov.open.jag.efilingaccountclient;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistry;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
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

    @InjectMocks
    CsoAccountServiceImpl sut;

    @Mock
    AccountFacade mockAccountFacade;

    @Mock
    AccountFacadeBean mockAccountFacadeBean;

    @Mock
    RoleRegistry mockRoleRegistry;

    @Mock
    RoleRegistryPortType mockRoleRegistryPortType;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        CsoAccountDetails details = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN);
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

        Mockito.when(mockAccountFacade.getAccountFacadeBeanPort()).thenReturn(mockAccountFacadeBean);
        Mockito.when(mockAccountFacadeBean.findProfiles(any())).thenReturn(profiles);
        Mockito.when(mockRoleRegistry.getRoleRegistrySourceRoleRegistryWsProviderRoleRegistryPort()).thenReturn(mockRoleRegistryPortType);
        Mockito.when(mockRoleRegistryPortType.getRolesForIdentifier("Courts", "CSO", USERGUIDWITHFILEROLE, "CAP")).thenReturn(userRolesWithFileRole);
        Mockito.when(mockRoleRegistryPortType.getRolesForIdentifier("Courts", "CSO", USERGUIDNOROLE, "CAP")).thenReturn(userRolesWithoutFileRole);
    }

    @DisplayName("getAccountDetails called with empty userGuid should return null")
    @Test
    public void testWithEmptyUserGuid() throws NestedEjbException_Exception {

        CsoAccountDetails details = sut.getAccountDetails("");
        Assertions.assertEquals(null, details);
    }

    @DisplayName("getAccountDetails called with userGuid with file role should return account details")
    @Test
    public void testWithFileRoleEnabled() {

        CsoAccountDetails details = sut.getAccountDetails(USERGUIDWITHFILEROLE);
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
    }
    
}
