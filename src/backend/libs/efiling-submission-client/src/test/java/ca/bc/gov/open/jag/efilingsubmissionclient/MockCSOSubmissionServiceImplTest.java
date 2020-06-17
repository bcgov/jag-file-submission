package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MockCSOSubmissionServiceImpl Test Suite")
public class MockCSOSubmissionServiceImplTest {
    @Test
    @DisplayName("CASE1: Test Mock Submission")
    public void testSubmission() {
        MockCSOSubmissionServiceImpl mockCSOSubmissionService = new MockCSOSubmissionServiceImpl();
        SubmitFilingResponse result = mockCSOSubmissionService.submitFiling(new FilingPackage());
        Assertions.assertEquals(BigDecimal.valueOf(1), result.getReturn());
    }
}
