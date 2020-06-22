package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.ag.csows.filing.FilingPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DemoSubmissionServiceImpl Test Suite")
public class DemoSubmissionServiceImplTest {
    @Test
    @DisplayName("CASE1: Test Demo Submission")
    public void testSubmission() {

        DemoSubmissionServiceImpl demoSubmissionService = new DemoSubmissionServiceImpl();
        BigDecimal result = demoSubmissionService.submitFiling(new FilingPackage());
        Assertions.assertEquals(BigDecimal.valueOf(1), result);
    }
}
