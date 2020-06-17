package ca.bc.gov.open.jag.efilingsubmissionclient;



import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ws.client.core.WebServiceOperations;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;


import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Facade Client Test Suite")
public class EfilingSubmissionClientTest {

    @InjectMocks
    CSOSubmissionServiceImpl cSOSubmissionService;

    @Mock
    WebServiceGatewaySupport webServiceOperations;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("CASE1: Test  Submission")
    public void testSubmission() {

        SubmitFilingResponse submitFilingResponse = new SubmitFilingResponse();
        submitFilingResponse.setReturn(new BigDecimal(1));
        when(webServiceOperations.getWebServiceTemplate().marshalSendAndReceive(any())).thenReturn(submitFilingResponse);
        SubmitFilingResponse result = cSOSubmissionService.submitFiling(new FilingPackage());
       // Assertions.assertEquals(BigDecimal.valueOf(1), result.getReturn());
    }

    @Test
    @DisplayName("CASE2: Test Failure Submission")
    public void testFailSubmission() {
        CSOSubmissionServiceImpl cSOSubmissionService = new CSOSubmissionServiceImpl();
        SubmitFilingResponse result = cSOSubmissionService.submitFiling(new FilingPackage());
        Assertions.assertNull(result.getReturn());
    }


}
