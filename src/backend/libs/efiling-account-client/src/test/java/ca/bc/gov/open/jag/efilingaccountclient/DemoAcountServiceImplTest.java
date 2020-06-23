package ca.bc.gov.open.jag.efilingaccountclient;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DemoAcountServiceImplTest {

    public static final UUID ACCOUNT_WITH_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID ACCOUNT_WITH_ADMIN_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    public static final UUID ACCOUNT_DOES_NOT_EXISTS = UUID.randomUUID();

    DemoAccountServiceImpl sut;

    @BeforeAll
    public void setup() {
        sut = new DemoAccountServiceImpl();
    }

    @Test
    @DisplayName("CASE 1: with account having efiling role")
    public void withAccountHavingEfilingRole() {

        CsoAccountDetails actual = sut.getAccountDetails(ACCOUNT_WITH_EFILING_ROLE.toString());

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertTrue(actual.HasRole("efiling"));

    }

    @Test
    @DisplayName("CASE 2: with account not having efiling role")
    public void withAccountHavingAdminRole() {

        CsoAccountDetails actual = sut.getAccountDetails(ACCOUNT_WITH_ADMIN_ROLE.toString());

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertFalse(actual.HasRole("efiling"));

    }

    @Test
    @DisplayName("CASE 3: with no account should return null")
    public void withNoAccountShouldBeNull() {

        Assertions.assertNull(sut.getAccountDetails(ACCOUNT_DOES_NOT_EXISTS.toString()));

    }

}
