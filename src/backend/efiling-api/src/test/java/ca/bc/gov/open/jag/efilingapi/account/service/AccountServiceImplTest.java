package ca.bc.gov.open.jag.efilingapi.account.service;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CreateAccountRequestMapper;
import ca.bc.gov.open.jag.efilingapi.account.mappers.CreateAccountRequestMapperImpl;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("AccountServiceImpl test suite")
public class AccountServiceImplTest {

    private static final String INTERNAL_CLIENT_NUMBER = "123456";
    private static final String FAIL_INTERNAL_CLIENT_NUMBER = "234567";
    public static final String BCEID = "BCEID";

    private AccountServiceImpl sut;

    @Mock
    private EfilingAccountService efilingAccountServiceMock;

    @BeforeAll
    public void setup() {

        MockitoAnnotations.initMocks(this);
        AccountDetails accountDetails = AccountDetails
                .builder()
                .accountId(BigDecimal.TEN)
                .create();


        Mockito
                .when(efilingAccountServiceMock.getAccountDetails(Mockito.eq(TestHelpers.CASE_1_STRING)))
                .thenReturn(accountDetails);

        Mockito
                .when(efilingAccountServiceMock.createAccount(Mockito.any()))
                .thenReturn(accountDetails);


        Mockito.doNothing().when(efilingAccountServiceMock).updateClient(any());


        // Testing mapper as part of the test
        CreateAccountRequestMapper createAccountRequestMapper = new CreateAccountRequestMapperImpl();
        sut = new AccountServiceImpl(efilingAccountServiceMock, createAccountRequestMapper);
    }

    @Test
    @DisplayName("OK: should return an account")
    public void withRequestShouldReturnAnAccount() {

        AccountDetails actual = sut.getCsoAccountDetails(TestHelpers.CASE_1_STRING);
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());

    }

    @Test
    @DisplayName("OK: execute method")
    public void withClientIdShouldExecuteMethod() {

        sut.updateClient(AccountDetails.builder()
                .internalClientNumber("123")
                .cardRegistered(true)
                .clientId(BigDecimal.TEN)
                .create());
        Mockito.verify(efilingAccountServiceMock,Mockito.times(1)).updateClient(any());

    }

    @Test
    @DisplayName("OK: should Create an account")
    public void withRequestShouldCreateAnAccount() {
        AccountDetails actual = sut.createAccount(UUID.randomUUID().toString(), BCEID, new CreateCsoAccountRequest());
        Mockito.verify(efilingAccountServiceMock,Mockito.times(1)).createAccount(any());

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
    }


}
