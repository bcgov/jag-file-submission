package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingPayment;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsostarter.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.FinancialTransactionMapper;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapper;

import java.math.BigDecimal;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {

    private final FilingFacadeBean filingFacadeBean;
    private final ServiceFacadeBean serviceFacadeBean;
    private final ServiceMapper serviceMapper;
    private final FilingPackageMapper filingPackageMapper;
    private final FinancialTransactionMapper financialTransactionMapper;
    private final CsoProperties csoProperties;


    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean, ServiceFacadeBean serviceFacadeBean, ServiceMapper serviceMapper, FilingPackageMapper filingPackageMapper, FinancialTransactionMapper financialTransactionMapper, CsoProperties csoProperties) {
        this.filingFacadeBean = filingFacadeBean;
        this.serviceFacadeBean = serviceFacadeBean;
        this.serviceMapper = serviceMapper;
        this.filingPackageMapper = filingPackageMapper;
        this.financialTransactionMapper = financialTransactionMapper;
        this.csoProperties = csoProperties;
    }

    @Override
    public BigDecimal submitFilingPackage(EfilingService service, EfilingFilingPackage filingPackage, EfilingPaymentService paymentService) {

        if(service == null) throw new IllegalArgumentException("Service is required.");
        if(filingPackage == null) throw new IllegalArgumentException("FilingPackage is required.");
        if(service.getClientId() == null) throw new IllegalArgumentException("Service id is required.");

        ServiceSession serviceSession = getServiceSession(service.getClientId().toString());

        Service createdService = createEfilingService(service, serviceSession);

        updatePaymentForService(
                createdService,
                true,
                createPayment(paymentService, createdService, service.getSubmissionFeeAmount(), service.getInternalClientNumber()));

        BigDecimal filingResult = filePackage(createdService, filingPackage);

        //TODO: do we add something to the next update
        updateServiceComplete(createdService);

        return filingResult;

    }

    private String generateInvoiceNumber(String data) {

        try {
            return serviceFacadeBean.getNextInvoiceNumber(data);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while generating next invoice number", e.getCause());
        }

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

    private FinancialTransaction createPayment(EfilingPaymentService paymentService, Service service, BigDecimal submissionFeeAmount, String internalClientNumber) {

        EfilingPayment efilingPayment = new EfilingPayment(service.getServiceId(), submissionFeeAmount, generateInvoiceNumber(Keys.INVOICE_PREFIX), internalClientNumber);
        EfilingTransaction payment = paymentService.makePayment(efilingPayment);
        return financialTransactionMapper.toTransaction(payment, service);

    }

    private BigDecimal filePackage(Service service, EfilingFilingPackage filingPackage) {

        // TODO: replace in the mapper when submission is a common object

        if(filingPackage.getDocuments() != null && !filingPackage.getDocuments().isEmpty()) {
            for(int i = 0; i < filingPackage.getDocuments().size(); i++) {
                filingPackage.getDocuments().get(i).setFileServer(csoProperties.getFileServerHost());
                filingPackage.getDocuments().get(i).setPackageSeqNo(new BigDecimal(i + 1));
            }
        }

        FilingPackage csoFilingPackage = filingPackageMapper.toFilingPackage(filingPackage, service.getServiceId());

        try {
            return filingFacadeBean.submitFiling(csoFilingPackage);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while filing package", e.getCause());
        }

    }

    private void updateServiceComplete(Service service) {
        service.setServiceReceivedDtm(DateUtils.getCurrentXmlDate());
        try {
            serviceFacadeBean.updateService(service);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while updating payment on service", e.getCause());
        }
    }

}
