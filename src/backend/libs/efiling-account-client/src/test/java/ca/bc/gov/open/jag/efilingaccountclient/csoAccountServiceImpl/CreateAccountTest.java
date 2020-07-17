package ca.bc.gov.open.jag.efilingaccountclient.csoAccountServiceImpl;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bceid.webservices.client.v9.BCeIDServiceSoap;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Create Account Test Suite")
public class CreateAccountTest {

    @Mock
    AccountFacadeBean accountFacadeBeanMock;

    @Mock
    RoleRegistryPortType roleRegistryPortTypeMock;

    @Mock
    BCeIDServiceSoap bCeIDServiceSoapMock;

    @Mock
    AccountDetailsMapper accountDetailsMapperMock;



    private CsoAccountServiceImpl sut;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, roleRegistryPortTypeMock, bCeIDServiceSoapMock, accountDetailsMapperMock);
    }

    @Test
    @DisplayName("No account details")
    public void notImplemented() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.createAccount(CreateAccountRequest.builder().create()));

    }

}
