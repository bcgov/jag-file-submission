package ca.bc.gov.open.jag.efilingcsostarter;

import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.filing.ProcessItemStatus;
import ca.bc.gov.ag.csows.filing.*;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsostarter.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {

    private final FilingFacadeBean filingFacadeBean;
    private final ServiceFacadeBean serviceFacadeBean;
    private final ServiceMapper serviceMapper;
    private final FilingPackageMapper filingPackageMapper;
    private final FinancialTransactionMapper financialTransactionMapper;
    private final CsoProperties csoProperties;
    private final DocumentMapper documentMapper;
    private final CsoPartyMapper csoPartyMapper;
    private final PackageAuthorityMapper packageAuthorityMapper;

    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean,
                                    ServiceFacadeBean serviceFacadeBean,
                                    ServiceMapper serviceMapper,
                                    FilingPackageMapper filingPackageMapper,
                                    FinancialTransactionMapper financialTransactionMapper,
                                    CsoProperties csoProperties,
                                    DocumentMapper documentMapper,
                                    CsoPartyMapper csoPartyMapper,
                                    PackageAuthorityMapper packageAuthorityMapper) {
        this.filingFacadeBean = filingFacadeBean;
        this.serviceFacadeBean = serviceFacadeBean;
        this.serviceMapper = serviceMapper;
        this.filingPackageMapper = filingPackageMapper;
        this.financialTransactionMapper = financialTransactionMapper;
        this.csoProperties = csoProperties;
        this.documentMapper = documentMapper;
        this.csoPartyMapper = csoPartyMapper;
        this.packageAuthorityMapper = packageAuthorityMapper;
    }

    @Override
    public SubmitPackageResponse submitFilingPackage(
            SubmitPackageRequest submitPackageRequest,
            EfilingPaymentService paymentService) {

        if (submitPackageRequest.getAccountDetails() == null) throw new IllegalArgumentException("Account Details are required");
        if (submitPackageRequest.getAccountDetails().getClientId() == null) throw new IllegalArgumentException("Client id is required.");
        if (submitPackageRequest.getFilingPackage() == null) throw new IllegalArgumentException("EfilingPackage is required.");

        ServiceSession serviceSession = getServiceSession(submitPackageRequest.getAccountDetails());

        Service createdService = createEfilingService(submitPackageRequest, serviceSession);

        updatePaymentForService(
                createdService,
                true,
                createPayment(
                        paymentService,
                        createdService,
                        submitPackageRequest.getFilingPackage().getSubmissionFeeAmount(),
                        submitPackageRequest.getAccountDetails().getInternalClientNumber()));

        ca.bc.gov.ag.csows.filing.FilingPackage csoFilingPackage = buildFilingPackage(submitPackageRequest, createdService);

        if (submitPackageRequest.getFilingPackage().isRushedSubmission()) {
            csoFilingPackage.setProcRequest(buildRushedOrderRequest(submitPackageRequest.getAccountDetails()));
        }

        BigDecimal filingResult = filePackage(csoFilingPackage);

        updateServiceComplete(createdService);

        return SubmitPackageResponse
                .builder()
                .packageLink(MessageFormat
                        .format("{0}/cso/accounts/bceidNotification.do?packageNo={1}", csoProperties.getCsoBasePath(), filingResult.toPlainString()))
                .transactionId(filingResult)
                .create();
    }

    private ca.bc.gov.ag.csows.filing.FilingPackage buildFilingPackage(SubmitPackageRequest submitPackageRequest, Service createdService) {
        XMLGregorianCalendar submittedDate = getComputedSubmittedDate(submitPackageRequest.getFilingPackage().getCourt().getLocation());
        return filingPackageMapper.toFilingPackage(
                        submitPackageRequest.getFilingPackage(),
                        submitPackageRequest.getAccountDetails(),
                        createdService.getServiceId(),
                        submittedDate,
                        buildCivilDocuments(submitPackageRequest, submittedDate),
                        buildCsoParties(submitPackageRequest),
                        buildPackageAuthorities(submitPackageRequest.getAccountDetails()));
    }

    private List<PackageAuthority> buildPackageAuthorities(AccountDetails accountDetails) {

        return Arrays.asList(packageAuthorityMapper.toPackageAuthority(accountDetails));

    }

    private List<CsoParty> buildCsoParties(SubmitPackageRequest submitPackageRequest) {

        List<CsoParty> csoParties = new ArrayList<>();

        for (int i = 0; i < submitPackageRequest.getFilingPackage().getParties().size(); i++) {
            csoParties.add(csoPartyMapper.toEfilingParties(i + 1, submitPackageRequest.getFilingPackage().getParties().get(i), submitPackageRequest.getAccountDetails()));
        }

        return csoParties;
    }


    private List<CivilDocument> buildCivilDocuments(SubmitPackageRequest submitPackageRequest, XMLGregorianCalendar submittedDate) {

        List<CivilDocument> documents = new ArrayList<>();

        for (int i = 0; i < submitPackageRequest.getFilingPackage().getDocuments().size(); i++) {

            List<DocumentPayments> payments = Arrays.asList(documentMapper.toEfilingDocumentPayment(submitPackageRequest.getFilingPackage().getDocuments().get(i), submitPackageRequest.getAccountDetails()));
            List<Milestones> milestones = Arrays.asList(documentMapper.toActualSubmittedDate(submitPackageRequest.getAccountDetails()),
                    documentMapper.toComputedSubmittedDate(submitPackageRequest.getAccountDetails(), submittedDate));
            List<DocumentStatuses> statuses = Arrays.asList(documentMapper.toEfilingDocumentStatus(submitPackageRequest.getFilingPackage().getDocuments().get(i), submitPackageRequest.getAccountDetails()));

            documents.add(documentMapper.toEfilingDocument(
                    i + 1,
                    submitPackageRequest.getFilingPackage().getDocuments().get(i),
                    submitPackageRequest.getAccountDetails(),
                    submitPackageRequest.getFilingPackage(),
                    csoProperties.getFileServerHost(),
                    milestones,
                    payments,
                    statuses
            ));
        }

        return documents;

    }

    private RushOrderRequest buildRushedOrderRequest(AccountDetails accountDetails) {
        RushOrderRequest processRequest = new RushOrderRequest();
        processRequest.setEntDtm(DateUtils.getCurrentXmlDate());
        processRequest.setEntUserId(accountDetails.getClientId().toString());
        processRequest.setRequestDt(DateUtils.getCurrentXmlDate());
        RushOrderRequestItem rushOrderRequestItem = new RushOrderRequestItem();
        rushOrderRequestItem.setEntDtm(DateUtils.getCurrentXmlDate());
        rushOrderRequestItem.setEntUserId(accountDetails.getClientId().toString());
        rushOrderRequestItem.setProcessReasonCd(Keys.RUSH_PROCESS_REASON_CD);
        rushOrderRequestItem.getItemStatuses().add(getProcessItemStatusRequest(accountDetails));
        rushOrderRequestItem.getItemStatuses().add(getProcessItemStatusApproved(accountDetails));
        processRequest.setItem(rushOrderRequestItem);
        return processRequest;
    }

    private ProcessItemStatus getProcessItemStatusRequest(AccountDetails accountDetails) {
        return getProcessItemStatus(accountDetails, Keys.REQUEST_PROCESS_STATUS_CD);
    }

    private ProcessItemStatus getProcessItemStatusApproved(AccountDetails accountDetails) {
        return getProcessItemStatus(accountDetails, Keys.APPROVED_PROCESS_STATUS_CD);
    }

    private ProcessItemStatus getProcessItemStatus(AccountDetails accountDetails, String proccessStatusCd) {
        ProcessItemStatus processItemStatus = new ProcessItemStatus();
        processItemStatus.setAccountId(accountDetails.getAccountId());
        processItemStatus.setClientId(accountDetails.getClientId());
        processItemStatus.setEntDtm(DateUtils.getCurrentXmlDate());
        processItemStatus.setEntUserId(accountDetails.getClientId().toString());
        processItemStatus.setProcessStatusCd(proccessStatusCd);
        return processItemStatus;
    }

    private String generateInvoiceNumber(String data) {

        try {
            return serviceFacadeBean.getNextInvoiceNumber(data);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while generating next invoice number", e.getCause());
        }

    }

    private ServiceSession getServiceSession(AccountDetails accountDetails) {
        try {
            UserSession userSession = serviceFacadeBean.createUserSession(accountDetails.getClientId().toString());
            return serviceFacadeBean.createServiceSession(userSession, "request");
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while getting user session", e.getCause());
        }
    }

    private Service createEfilingService(SubmitPackageRequest submitPackageRequest, ServiceSession serviceSession) {

        Service serviceToCreate = serviceMapper.toCreateService(submitPackageRequest.getFilingPackage(), submitPackageRequest.getAccountDetails(), serviceSession);

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
        PaymentTransaction payment = paymentService.makePayment(efilingPayment);
        return financialTransactionMapper.toTransaction(payment, service);

    }

    private BigDecimal filePackage(ca.bc.gov.ag.csows.filing.FilingPackage csoFilingPackage) {
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

    private XMLGregorianCalendar getComputedSubmittedDate(String location) {
        try {
            return filingFacadeBean.calculateSubmittedDate(DateUtils.getCurrentXmlDate(), location);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while retrieving submitted date", e.getCause());
        }
    }

}
