package ca.bc.gov.open.jag.efilingcsostarter.csoAccountServiceImpl;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistry;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcsostarter.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingcsostarter.CsoHelpers;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.AccountDetailsMapper;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetOrderNumber")
public class GetOrderNumberTest {

    private static final UUID USER_GUID_NO_ROLE = UUID.randomUUID();
    private static final UUID USER_GUID_WITH_FILE_ROLE = UUID.randomUUID();
    private static final UUID USER_GUID_WITH_NO_CSO = UUID.randomUUID();
    private static final UUID USER_GUID_WITH_EJB_EXCEPTION = UUID.randomUUID();
    private static final UUID USER_GUID_WITH_MULTI_PROFILE = UUID.randomUUID();
    public static final String DOMAIN = "Courts";
    public static final String APPLICATION = "CSO";
    public static final String IDENTIFIER_TYPE = "CAP";
    private static final String INTERNAL_CLIENT_NUMBER = "123";

    CsoAccountServiceImpl sut;


    @Mock
    AccountFacadeBean accountFacadeBeanMock;


    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, null, null);
    }



    @DisplayName("OK: getNextOrderNumber")
    @Test
    public void testNextOrderReturned() throws NestedEjbException_Exception {

        Mockito.when(accountFacadeBeanMock.getNextOrderNumber()).thenReturn(BigDecimal.TEN);

        String actual = sut.getOrderNumber();

        Assertions.assertEquals(BigDecimal.TEN.toString(), actual);
    }


    @DisplayName("Exception: when soap service throws exception throw EfilingAccountServiceException")
    @Test
    public void withMultiProflieShouldThrowCSOHasMultipleAccountException() throws NestedEjbException_Exception {
        Mockito.when(accountFacadeBeanMock.getNextOrderNumber()).thenThrow(NestedEjbException_Exception.class);

        Assertions.assertThrows(EfilingAccountServiceException.class, () -> {
            sut.getOrderNumber();
        });


    }
}
