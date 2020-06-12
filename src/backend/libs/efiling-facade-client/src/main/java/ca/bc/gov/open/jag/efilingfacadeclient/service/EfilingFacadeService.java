package ca.bc.gov.open.jag.efilingfacadeclient.service;

import ca.bc.gov.open.jag.ag.csows.filing.FilingPackage;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFiling;
import ca.bc.gov.open.jag.ag.csows.filing.SubmitFilingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class EfilingFacadeService extends WebServiceGatewaySupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(EfilingFacadeService.class);

    public BigDecimal SubmitFiling(String strFileInfo) {

        SubmitFiling request = new SubmitFiling();
        FilingPackage filingPackage = new FilingPackage();

        // TODO - Need to map the fields into the filingPackage
        filingPackage.setApplicationReferenceGuid(UUID.randomUUID().toString());
        filingPackage.setApplicationCd(strFileInfo);

        request.setArg0(filingPackage);

        try {

            LOGGER.info("Calling SubmitFiling with package id: " + filingPackage.getApplicationReferenceGuid());
            SubmitFilingResponse response =
                    (SubmitFilingResponse) getWebServiceTemplate().marshalSendAndReceive(request);
            return response.getReturn();

        } catch (Exception e) {

            LOGGER.error("Error calling SubmitFiling: " + e.toString());
            return BigDecimal.ZERO;
        }
    }
}
