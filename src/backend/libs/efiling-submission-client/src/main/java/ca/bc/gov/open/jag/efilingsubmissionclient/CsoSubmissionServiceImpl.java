package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.ag.csows.filing.FilingFacade;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CsoSubmissionServiceImpl implements EfilingSubmissionService {

    private FilingFacade filingFacade;
    private static final Logger LOGGER = LoggerFactory.getLogger(CsoSubmissionServiceImpl.class);

    public CsoSubmissionServiceImpl(FilingFacade filingFacade) {

        this.filingFacade = filingFacade;
    }

    @Override
    public BigDecimal submitFiling(FilingPackage filingPackage) {

        // TODO - validate incoming package?

        FilingFacadeBean port = filingFacade.getFilingFacadeBeanPort();

        try {
            return port.submitFiling(filingPackage);
        } catch (NestedEjbException_Exception e) {
            LOGGER.error("Expected exception: NestedEjbException has occurred :" + e.toString());
            return BigDecimal.valueOf(-1);
        }
    }
}
