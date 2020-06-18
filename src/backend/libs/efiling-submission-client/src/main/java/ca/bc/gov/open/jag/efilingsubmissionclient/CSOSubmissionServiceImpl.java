package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFiling;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class CSOSubmissionServiceImpl extends WebServiceGatewaySupport implements EfilingSubmissionService {

    private static final Logger _logger = LoggerFactory.getLogger(CSOSubmissionServiceImpl.class);

    @Override
    public SubmitFilingResponse submitFiling(FilingPackage filingPackage) {

        // TODO - Validate package contents?

        SubmitFiling request = new SubmitFiling();
        request.setArg0(filingPackage);

        SubmitFilingResponse submitFilingResponse = new SubmitFilingResponse();

        try {

            _logger.info("Calling SubmitFiling with package id: {}", filingPackage.getApplicationReferenceGuid());
            submitFilingResponse =  (SubmitFilingResponse)getWebServiceTemplate().marshalSendAndReceive(request);
        } catch (Exception e) {

            _logger.error("Error calling SubmitFiling: ", e);
        }

        return submitFilingResponse;
    }
}
