package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapper;

import java.math.BigDecimal;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {
    private final FilingFacadeBean filingFacadeBean;
    private final ServiceFacadeBean serviceFacadeBean;
    private final ServiceMapper serviceMapper;
    private final FilingPackageMapper filingPackageMapper;

    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean, ServiceFacadeBean serviceFacadeBean, ServiceMapper serviceMapper, FilingPackageMapper filingPackageMapper) {
        this.filingFacadeBean = filingFacadeBean;
        this.serviceFacadeBean = serviceFacadeBean;
        this.serviceMapper = serviceMapper;
        this.filingPackageMapper = filingPackageMapper;
    }

    @Override
    public BigDecimal submitFilingPackage(EfilingService service, EfilingFilingPackage filingPackage) {
        if(service == null) throw new IllegalArgumentException("Service  cannot be null");
        if(filingPackage == null) throw new IllegalArgumentException("Filing Package cannot be null");
        //TODO: add required information for the filing package
        try {
            //Add service
            EfilingService addedService = serviceMapper.toEfilingService(serviceFacadeBean.addService(serviceMapper.toService(service)));
            //TODO: make payments to bambora
            //Update service with payments
            serviceFacadeBean.updateService(serviceMapper.toService(addedService));
            //Submit filing package
            FilingPackage csoFilingPackage = filingPackageMapper.toFilingPackage(filingPackage);
            csoFilingPackage.setServiceId(addedService.getServiceId());
            BigDecimal filingResult = filingFacadeBean.submitFiling(csoFilingPackage);
            //TODO: do we add something to the next update
            //Update service after filing submission
            serviceFacadeBean.updateService(serviceMapper.toService(addedService));
            return filingResult;
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while submitting filing", e.getCause());
        }  catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while creating service", e.getCause());
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

}
