package ca.bc.gov.open.jag.efilingcsostarter.csoAccountServiceImpl;

import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcsostarter.CsoAccountServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UpdateClient")
public class UpdateClientTest {

    private static final String INTERNAL_CLIENT_NUMBER = "123";
    private static final String FAIL_INTERNAL_CLIENT_NUMBER = "1234";
    CsoAccountServiceImpl sut;

    @Mock
    AccountFacadeBean accountFacadeBeanMock;


    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);
        initAccountFacadeMocks();

        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, null, null);
    }

    private void initAccountFacadeMocks() throws NestedEjbException_Exception {

        Mockito.doReturn(null).when(accountFacadeBeanMock)
                .updateClient(ArgumentMatchers.argThat(x -> x.getInternalClientNo().equals(INTERNAL_CLIENT_NUMBER)));


        Mockito.doThrow(NestedEjbException_Exception.class).when(accountFacadeBeanMock)
                .updateClient(ArgumentMatchers.argThat(x -> x.getInternalClientNo().equals(FAIL_INTERNAL_CLIENT_NUMBER)));
    }

    @DisplayName("OK: update client executes")
    @Test
    public void testWithClientNumberUpdateClientExecuted() throws NestedEjbException_Exception {

        sut.updateClient(INTERNAL_CLIENT_NUMBER);

        Mockito.verify(accountFacadeBeanMock,Mockito.times(1)).updateClient(ArgumentMatchers.argThat(x -> x.getInternalClientNo().equals(INTERNAL_CLIENT_NUMBER)));
    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingAccountServiceException")
    @Test
    public void withNestedEjbExceptionExceptionShouldThrowEfilingAccountServiceException() {


        Assertions.assertThrows(EfilingAccountServiceException.class, () -> {
            sut.updateClient(FAIL_INTERNAL_CLIENT_NUMBER);
        });

    }

    @DisplayName("Exception: with null client id should throw IllegalArgumentException")
    @Test
    public void withNullClientIdReturnIllegalArgumentException() {


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sut.updateClient(null);
        });

    }
}
