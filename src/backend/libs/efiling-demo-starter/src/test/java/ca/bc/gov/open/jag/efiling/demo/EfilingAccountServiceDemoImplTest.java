package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingAccountServiceDemoImplTest {

    public static final UUID ACCOUNT_WITH_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID ACCOUNT_WITHOUT_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");

    EfilingAccountServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingAccountServiceDemoImpl();
    }

    @Test
    @DisplayName("OK: should return id with file role")
    public void id1ShouldReturnAccountWithFileRole() {

        AccountDetails actual = sut.getAccountDetails(ACCOUNT_WITH_EFILING_ROLE, "");

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertEquals(true, actual.isFileRolePresent());

    }

    @Test
    @DisplayName("OK: should return id without file role")
    public void id2ShouldReturnAccountWithoutFileRole() {

        AccountDetails actual = sut.getAccountDetails(ACCOUNT_WITHOUT_EFILING_ROLE, "");

        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertEquals(false, actual.isFileRolePresent());
    }

    @Test
    @DisplayName("Not Implemented!")
    public void notImplemented() {

        Assertions.assertThrows(NotImplementedException.class, () -> sut.createAccount(CreateAccountRequest.builder().create()));

    }
}
