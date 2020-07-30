package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapper;

import java.math.BigDecimal;
import java.util.UUID;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {
    private final FilingFacadeBean filingFacadeBean;
    private final ServiceFacadeBean serviceFacadeBean;
    private final ServiceMapper serviceMapper;

    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean, ServiceFacadeBean serviceFacadeBean, ServiceMapper serviceMapper) {
        this.filingFacadeBean = filingFacadeBean;
        this.serviceFacadeBean = serviceFacadeBean;
        this.serviceMapper = serviceMapper;
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
            return serviceMapper.toEfilingService(serviceFacadeBean.addService(serviceMapper.toService(service)));
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while creating service", e.getCause());
        }

    }

    @Override
    public void updateService(EfilingService service) {
        if (service == null) throw new IllegalArgumentException("Service cannot be null");
        if (service.getTransactions() == null || service.getTransactions().isEmpty()) throw new IllegalArgumentException("Service requires transactions");
        try {
            serviceFacadeBean.updateService(serviceMapper.toService(service));
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while creating service", e.getCause());
        }
    }
}
