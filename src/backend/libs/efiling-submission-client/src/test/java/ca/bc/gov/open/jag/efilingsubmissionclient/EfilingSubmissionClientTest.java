package ca.bc.gov.open.jag.efilingsubmissionclient;



import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;

import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Facade Client Test Suite")
public class EfilingSubmissionClientTest {

    @Autowired
    CSOSubmissionServiceImpl cSOSubmissionService;


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
