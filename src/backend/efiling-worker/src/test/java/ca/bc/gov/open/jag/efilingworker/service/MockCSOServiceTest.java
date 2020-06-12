package ca.bc.gov.open.jag.efilingworker.service;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
@DisplayName("MockCSOService submitFiling Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MockCSOServiceTest {

    private MockCSOService sut;

    @BeforeAll
    public void setUp() {
        sut = new MockCSOService();
    }

    @Test
    @DisplayName("CASE1: with any request should return 1")
    public void withAnyRequestShouldReturnSeven() {

        SubmitFilingResponse actual = sut.submitFiling(new FilingPackage());

        Assertions.assertEquals(new BigDecimal(1), actual.getReturn());

    }

}

