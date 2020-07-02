package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingAccountServiceDemoImplTest {

    public static final UUID ACCOUNT_WITH_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID ACCOUNT_WITHOUT_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    public static final UUID ACCOUNT_DOES_NOT_EXISTS = UUID.fromString("88da92db-0791-491e-8c58-1a969e67d2fb");

    EfilingAccountServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingAccountServiceDemoImpl();
    }

    @Test
    @DisplayName("CASE 1: with account having efiling role")
    public void withAccountHavingEfilingRole() {

        AccountDetails actual = sut.getAccountDetails(ACCOUNT_WITH_EFILING_ROLE.toString(), "");

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertEquals(true, actual.isFileRolePresent());
    }

    @Test
    @DisplayName("CASE 2: with account not having efiling role")
    public void withAccountHavingAdminRole() {

        AccountDetails actual = sut.getAccountDetails(ACCOUNT_WITHOUT_EFILING_ROLE.toString(), "");

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertEquals(false, actual.isFileRolePresent());
    }

    @Test
    @DisplayName("CASE 3: with no account should return null")
    public void withNoAccountShouldBeNull() {

        AccountDetails actual = sut.getAccountDetails(ACCOUNT_DOES_NOT_EXISTS.toString(), "");

        Assertions.assertEquals(BigDecimal.ZERO, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.ZERO, actual.getClientId());
        Assertions.assertEquals(false, actual.isFileRolePresent());
        Assertions.assertEquals("Bob", actual.getFirstName());
        Assertions.assertEquals("Rob", actual.getMiddleName());
        Assertions.assertEquals("Ross", actual.getLastName());
        Assertions.assertEquals("bross@paintit.com", actual.getEmail());
    }

}
