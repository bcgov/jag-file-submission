package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import java.math.BigDecimal;
import java.util.UUID;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {
    private final FilingFacadeBean filingFacadeBean;

    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean) {
        this.filingFacadeBean = filingFacadeBean;
    }

    @Override
    public BigDecimal submitFilingPackage(UUID submissionId) {
        if(submissionId == null) throw new IllegalArgumentException("Submission id cannot be null");
        FilingPackage filingPackage = new FilingPackage();

        try {
            return filingFacadeBean.submitFiling(filingPackage);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while retrieving submitting filing", e.getCause());
        }
    }
}
