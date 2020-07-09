package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import org.junit.jupiter.api.*;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingAccountServiceDemoImplTest {

    public static final UUID ACCOUNT_ID = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final String FIRST_NAME = "firstName";

    EfilingAccountServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingAccountServiceDemoImpl();
    }

    @Test
    @DisplayName("OK: with empty cache should return null")
    public void withEmptyCacheShouldReturnNull() {

        AccountDetails actual = sut.getAccountDetails(ACCOUNT_ID, "");
        Assertions.assertNull(actual);

    }

    @Test
    @DisplayName("OK: should create an account")
    public void shouldCreateAnAccount() {

         AccountDetails actual = sut.createAccount(CreateAccountRequest.builder().universalId(ACCOUNT_ID).firstName(FIRST_NAME).create());
         Assertions.assertEquals(ACCOUNT_ID, actual.getUniversalId());
         Assertions.assertEquals(FIRST_NAME, actual.getFirstName());

    }

}
