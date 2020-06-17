package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Facade Client Test Suite")
public class EfilingSubmissionClientTest {
    @Test
    @DisplayName("CASE1: Test  Submission")
    public void testSubmission() {

    }

    @Test
    @DisplayName("CASE2: Test Failure Submission")
    public void testFailSubmission() {
        CSOSubmissionServiceImpl cSOSubmissionService = new CSOSubmissionServiceImpl();
        SubmitFilingResponse result = cSOSubmissionService.submitFiling(new FilingPackage());
        Assertions.assertNull(result.getReturn());
    }


}
