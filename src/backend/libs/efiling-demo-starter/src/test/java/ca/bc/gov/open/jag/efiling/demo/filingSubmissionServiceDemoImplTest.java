package ca.bc.gov.open.jag.efiling.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Submission Client Test Suite")
public class filingSubmissionServiceDemoImplTest {
    @DisplayName("CASE 1: Testing Demo getServiceFee")
    @Test
    public void testDemoLookupServiceTest() {

        EfilingSubmissionServiceDemoImpl service = new EfilingSubmissionServiceDemoImpl();

        BigDecimal actual = service.submitFilingPackage(UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe"));

        Assertions.assertEquals(BigDecimal.ONE, actual);

    }
}
