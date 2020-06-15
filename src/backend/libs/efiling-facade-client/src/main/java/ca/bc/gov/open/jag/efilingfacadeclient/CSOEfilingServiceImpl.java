package ca.bc.gov.open.jag.efilingfacadeclient;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFiling;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class CSOEfilingServiceImpl extends WebServiceGatewaySupport implements EfilingFacadeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSOEfilingServiceImpl.class);

    @Override
    public SubmitFilingResponse submitFiling(FilingPackage filingPackage) {

        // TODO - Validate package contents?

        SubmitFiling request = new SubmitFiling();
        request.setArg0(filingPackage);

        SubmitFilingResponse submitFilingResponse = new SubmitFilingResponse();

        try {

            LOGGER.info("Calling SubmitFiling with package id: " + filingPackage.getApplicationReferenceGuid());
            submitFilingResponse =  (SubmitFilingResponse)getWebServiceTemplate().marshalSendAndReceive(request);
        } catch (Exception e) {

            LOGGER.error("Error calling SubmitFiling: " + e.toString());
        }

        return submitFilingResponse;
    }
}
