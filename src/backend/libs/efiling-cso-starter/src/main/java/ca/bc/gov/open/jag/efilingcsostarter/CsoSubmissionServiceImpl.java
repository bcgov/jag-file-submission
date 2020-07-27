package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import java.math.BigDecimal;
import java.util.UUID;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {
    private final FilingFacadeBean filingFacadeBean;
    private final ServiceFacadeBean serviceFacadeBean;

    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean, ServiceFacadeBean serviceFacadeBean) {
        this.filingFacadeBean = filingFacadeBean;
        this.serviceFacadeBean = serviceFacadeBean;
    }

    @Override
    public BigDecimal submitFilingPackage(UUID submissionId) {
        if(submissionId == null) throw new IllegalArgumentException("Submission id cannot be null");
        FilingPackage filingPackage = new FilingPackage();
        //TODO: add required information for the filing package
        try {
            return filingFacadeBean.submitFiling(filingPackage);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while submitting filing", e.getCause());
        }
    }

    @Override
    public EfilingService addService(EfilingService service) {
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        try {
            serviceFacadeBean.addService(null);
            return service;
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while creating service", e.getCause());
        }

    }
}
