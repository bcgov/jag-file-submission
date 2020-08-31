package ca.bc.gov.open.jag.efiling.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Submission Client Test Suite")
public class EfilingSubmissionServiceDemoImplTest {
    @DisplayName("CASE 1: Testing Demo submitFilingPackage")
    @Test
    public void testDemoLookupFilingPackageServiceTest() {

        EfilingSubmissionServiceDemoImpl service = new EfilingSubmissionServiceDemoImpl();

        BigDecimal actual = service.submitFilingPackage(null, null, null, false, null);

        Assertions.assertEquals(BigDecimal.ONE, actual);

    }
}
