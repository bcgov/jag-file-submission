package ca.bc.gov.open.jag.efilingfacadeclient;

import ca.bc.gov.open.jag.efilingfacadeclient.service.EfilingFacadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


public class EfilingFacadeClient  {

    private static final Logger LOGGER = LoggerFactory.getLogger(EfilingFacadeClient.class);
    private static final EfilingFacadeService efilingFacadeService = new EfilingFacadeService();

    // TODO - the strFileInfo will need to be real params for the SOAP call when we know them
    public BigDecimal SubmitFiling(String strFileInfo/* TODO - This will require some real data */) {

        LOGGER.info("Calling into efilingFacadeService");
        return efilingFacadeService.SubmitFiling(strFileInfo);
    }
}
