package ca.bc.gov.open.jagefilingapi.fee.mockService;

import ca.bc.gov.open.jagefilingapi.fee.MockFeeService;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@DisplayName("MockFeeService getFee Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetFeeTest {

    public MockFeeService sut;

    @BeforeAll
    public void setUp() {
        sut = new MockFeeService();
    }

    @Test
    @DisplayName("CASE1: with any request should return 7")
    public void withAnyRequestShouldReturnSeven() {

        Fee actual = sut.getFee(new FeeRequest("type", "subtype"));

        Assertions.assertEquals(new BigDecimal(7), actual.getAmount());

    }

}
