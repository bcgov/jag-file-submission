package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingAccountServiceDemoImplTest {

    public static final String ACCOUNT_ID = "77da92db-0791-491e-8c58-1a969e67d2fa";
    public static final String ACCOUNT_ID_NOT_EXIST = "17da92db-0791-491e-8c58-1a969e67d2fa";
    public static final String FIRST_NAME = "firstName";

    private EfilingAccountServiceDemoImpl sut;

    @Mock
    private AccountDetailsCache accountDetailsCacheMock;

    @BeforeAll
    public void setup() {

        MockitoAnnotations.openMocks(this);

        AccountDetails accountDetails = AccountDetails.builder().accountId(BigDecimal.ONE).universalId(ACCOUNT_ID).create();

        Mockito.when(accountDetailsCacheMock.get(Mockito.eq(ACCOUNT_ID))).thenReturn(accountDetails);
        Mockito.when(accountDetailsCacheMock.put(ArgumentMatchers.argThat(x -> x.getUniversalId() == ACCOUNT_ID))).thenReturn(accountDetails);

        sut = new EfilingAccountServiceDemoImpl(accountDetailsCacheMock);
    }

    @Test
    @DisplayName("OK: with empty cache should return null")
    @Order(1)
    public void withEmptyCacheShouldReturnNull() {
        AccountDetails actual = sut.getAccountDetails(ACCOUNT_ID_NOT_EXIST);
        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("OK: should create an account")
    @Order(2)
    public void shouldCreateAnAccount() {

         AccountDetails actual = sut.createAccount(CreateAccountRequest.builder().universalId(ACCOUNT_ID).firstName(FIRST_NAME).create());
         Assertions.assertEquals(ACCOUNT_ID, actual.getUniversalId());
    }

    @Test
    @DisplayName("OK: should return order number as string")
    @Order(3)
    public void shouldReturnOrderNumber() {
        String actual = sut.getOrderNumber();
        Assertions.assertEquals("1234", actual);
    }

    @Test
    @DisplayName("OK: should put account in cache")
    @Order(3)
    public void shouldPutAAccountinCache() {
        Assertions.assertDoesNotThrow(() -> sut.updateClient(AccountDetails.builder().clientId(BigDecimal.TEN).create()));
    }

}
