package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
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
    public BigDecimal submitFilingPackage(EfilingService service, EfilingFilingPackage filingPackage, EfilingPaymentService paymentService) {

        if(service == null) throw new IllegalArgumentException("Service is required.");
        if(filingPackage == null) throw new IllegalArgumentException("FilingPackage is required.");
        if(service.getClientId() == null) throw new IllegalArgumentException("Service id is required.");

        ServiceSession serviceSession = getServiceSession(service.getClientId().toString());

        Service createdService = createEfilingService(service, serviceSession);

        //TODO: make payments to bambora
        updatePaymentForService(createdService, true, new FinancialTransaction());

        BigDecimal filingResult = filePackage(service, filingPackage);

        //TODO: do we add something to the next update
        updateServiceComplete(createdService);

        return filingResult;

    }

    private ServiceSession getServiceSession(String clientId)  {
        try {
            UserSession userSession = serviceFacadeBean.createUserSession(clientId);
            return serviceFacadeBean.createServiceSession(userSession, "request");
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while getting user session", e.getCause());
        }
    }

    private Service createEfilingService(EfilingService service, ServiceSession serviceSession) {
        Service serviceToCreate = serviceMapper.toService(service, serviceSession);
        try {
            return serviceFacadeBean.addService(serviceToCreate);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while creating efiling service", e.getCause());
        }
    }

    private void updatePaymentForService(Service service, Boolean feePaid, FinancialTransaction financialTransaction) {

        service.setFeePaidYn(String.valueOf(feePaid));
        service.getTransactions().add(financialTransaction);

        try {
            serviceFacadeBean.updateService(service);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while updating payment on service", e.getCause());
        }

    }

    private BigDecimal filePackage(EfilingService service, EfilingFilingPackage filingPackage) {
        FilingPackage csoFilingPackage = filingPackageMapper.toFilingPackage(filingPackage, service.getServiceId());
        try {
            return filingFacadeBean.submitFiling(csoFilingPackage);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while filing package", e.getCause());
        }
    }

    private void updateServiceComplete(Service service) {
        // TODO implement update service
    }

}
