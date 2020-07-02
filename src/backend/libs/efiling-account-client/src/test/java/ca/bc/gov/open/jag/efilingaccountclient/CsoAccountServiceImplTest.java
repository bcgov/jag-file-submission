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
import ca.bceid.webservices.client.v9.*;
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
    private static final String USERGUIDWITHNOCSO = "USERGUIDWITHNOCSO";

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
    BCeIDService bCeIDService;

    @Mock
    BCeIDServiceSoap bCeIDServiceSoap;

    @Mock
    AccountDetailsMapper accountDetailsMapperMock;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        //////////////////////////////////////////////////////////////////////////////////////
        // Setup the account facade classes for the mock calls
        ClientProfile profile =  new ClientProfile();
        profile.setAccountId(BigDecimal.TEN);
        profile.setClientId(BigDecimal.TEN);
        List<ClientProfile> profiles = new ArrayList<ClientProfile>();
        profiles.add(profile);

        List<ClientProfile> emptyProfiles = new ArrayList<ClientProfile>();

        Mockito.when(accountFacadeMock.getAccountFacadeBeanPort()).thenReturn(accountFacadeBeanMock);
        Mockito.when(accountFacadeBeanMock.findProfiles(USERGUIDNOROLE)).thenReturn(profiles);
        Mockito.when(accountFacadeBeanMock.findProfiles(USERGUIDWITHFILEROLE)).thenReturn(profiles);
        Mockito.when(accountFacadeBeanMock.findProfiles(USERGUIDWITHNOCSO)).thenReturn(emptyProfiles);

        //////////////////////////////////////////////////////////////////////////////////////
        // Setup the role registry classes and mock calls
        RegisteredRole fileRole = new RegisteredRole();
        fileRole.setCode("FILE");
        UserRoles userRolesWithFileRole = new UserRoles();
        userRolesWithFileRole.getRoles().add(fileRole);

        RegisteredRole notFileRole = new RegisteredRole();
        notFileRole.setCode("NOTFILE");
        UserRoles userRolesWithoutFileRole = new UserRoles();
        userRolesWithoutFileRole.getRoles().add(notFileRole);


        Mockito.when(roleRegistryMock.getRoleRegistrySourceRoleRegistryWsProviderRoleRegistryPort()).thenReturn(roleRegistryPortTypeMock);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier("Courts", "CSO", USERGUIDWITHFILEROLE, "CAP")).thenReturn(userRolesWithFileRole);
        Mockito.when(roleRegistryPortTypeMock.getRolesForIdentifier("Courts", "CSO", USERGUIDNOROLE, "CAP")).thenReturn(userRolesWithoutFileRole);

        AccountDetails csoUserDetailsWithRole = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "firstName", "lastName", "middleName", "email");
        Mockito.when(accountDetailsMapperMock.toAccountDetails(Mockito.any(), Mockito.eq(true))).thenReturn(csoUserDetailsWithRole);

        AccountDetails csoUserDetailsWithoutRole = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, false, "firstName", "lastName", "middleName","email");
        AccountDetails accountDetailsWithNoCso = new AccountDetails(BigDecimal.ZERO, BigDecimal.ZERO, false, "firstName", "lastName", "middleName","email");
        Mockito.when(accountDetailsMapperMock.toAccountDetails(Mockito.any(), Mockito.eq(false))).thenReturn(csoUserDetailsWithoutRole);

        //////////////////////////////////////////////////////////////////////////////////////
        // Setup the BCeID account classes and mock calls
        BCeIDAccountContact contact = new BCeIDAccountContact();
        BCeIDString str = new BCeIDString();
        str.setValue("email@email.com");
        contact.setEmail(str);

        BCeIDName name = new BCeIDName();
        str.setValue("first");
        name.setFirstname(str);
        str.setValue("middle");
        name.setMiddleName(str);
        str.setValue("surname");
        name.setSurname(str);

        BCeIDIndividualIdentity identity = new BCeIDIndividualIdentity();
        identity.setName(name);

        BCeIDAccount bCeIDAccount = new BCeIDAccount();
        bCeIDAccount.setContact(contact);
        bCeIDAccount.setIndividualIdentity(identity);

        AccountDetailResponse bCeIDResponse = new AccountDetailResponse();
        bCeIDResponse.setCode(ResponseCode.SUCCESS);
        bCeIDResponse.setAccount(bCeIDAccount);

        Mockito.when(bCeIDService.getBCeIDServiceSoap()).thenReturn(bCeIDServiceSoap);
        Mockito.when(bCeIDServiceSoap.getAccountDetail(any())).thenReturn(bCeIDResponse);
        Mockito.when(accountDetailsMapperMock.toAccountDetails(Mockito.any())).thenReturn(accountDetailsWithNoCso);

        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, roleRegistryPortTypeMock, bCeIDServiceSoap, accountDetailsMapperMock);

    }

    @DisplayName("getAccountDetails called with empty userGuid")
    @Test
    public void testWithEmptyUserGuid() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails("", "");
        Assertions.assertEquals(null, details);
    }

    @DisplayName("getAccountDetails called with userGuid with file role")
    @Test
    public void testWithFileRoleEnabled() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USERGUIDWITHFILEROLE, "");
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
        Assertions.assertEquals(true, details.isFileRolePresent());
        Assertions.assertEquals("firstName", details.getFirstName());
        Assertions.assertEquals("lastName", details.getLastName());
        Assertions.assertEquals("email", details.getEmail());

    }

    @DisplayName("getAccountDetails called with a userGuid that does not have file role")
    @Test
    public void testWithFileRoleDisabled() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USERGUIDNOROLE, "");
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.TEN, details.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, details.getClientId());
        Assertions.assertEquals(false, details.isFileRolePresent());
        Assertions.assertEquals("firstName", details.getFirstName());
        Assertions.assertEquals("lastName", details.getLastName());
        Assertions.assertEquals("email", details.getEmail());

    }

    @DisplayName("getAccountDetails called with a userGuid that does not have cso account")
    @Test
    public void testWithNoCsoAccount() throws NestedEjbException_Exception {

        AccountDetails details = sut.getAccountDetails(USERGUIDWITHNOCSO, "Individual");
        Assertions.assertNotEquals(null, details);
        Assertions.assertEquals(BigDecimal.ZERO, details.getAccountId());
        Assertions.assertEquals(BigDecimal.ZERO, details.getClientId());
        Assertions.assertEquals(false, details.isFileRolePresent());
        Assertions.assertEquals("firstName", details.getFirstName());
        Assertions.assertEquals("lastName", details.getLastName());
        Assertions.assertEquals("email", details.getEmail());
    }
}
