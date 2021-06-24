package ca.bc.gov.open.jag.efilingcsoclient.csoAccountServiceImpl;

import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcsoclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.ClientProfileMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetOrderNumber")
public class GetOrderNumberTest {


    CsoAccountServiceImpl sut;


    @Mock
    AccountFacadeBean accountFacadeBeanMock;


    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);

        sut = new CsoAccountServiceImpl(accountFacadeBeanMock, null, null, new ClientProfileMapperImpl());
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
