package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.Document;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.filing.ProcessItemStatus;
import ca.bc.gov.ag.csows.filing.*;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static ca.bc.gov.open.jag.efilingcsoclient.CsoHelpers.xmlGregorian2Date;
import static ca.bc.gov.open.jag.efilingcsoclient.Keys.*;

public class CsoSubmissionServiceImpl implements EfilingSubmissionService {

    Logger logger = LoggerFactory.getLogger(CsoSubmissionServiceImpl.class);

    private final FilingFacadeBean filingFacadeBean;
    private final ServiceFacadeBean serviceFacadeBean;
    private final ServiceMapper serviceMapper;
    private final FilingPackageMapper filingPackageMapper;
    private final FinancialTransactionMapper financialTransactionMapper;
    private final CsoProperties csoProperties;
    private final DocumentMapper documentMapper;
    private final CsoPartyMapper csoPartyMapper;
    private final PackageAuthorityMapper packageAuthorityMapper;
    private final EfilingDocumentService efilingDocumentService;

    public CsoSubmissionServiceImpl(FilingFacadeBean filingFacadeBean,
                                    ServiceFacadeBean serviceFacadeBean,
                                    ServiceMapper serviceMapper,
                                    FilingPackageMapper filingPackageMapper,
                                    FinancialTransactionMapper financialTransactionMapper,
                                    CsoProperties csoProperties,
                                    DocumentMapper documentMapper, CsoPartyMapper csoPartyMapper, PackageAuthorityMapper packageAuthorityMapper, EfilingDocumentService efilingDocumentService) {
        this.filingFacadeBean = filingFacadeBean;
        this.serviceFacadeBean = serviceFacadeBean;
        this.serviceMapper = serviceMapper;
        this.filingPackageMapper = filingPackageMapper;
        this.financialTransactionMapper = financialTransactionMapper;
        this.csoProperties = csoProperties;
        this.documentMapper = documentMapper;
        this.csoPartyMapper = csoPartyMapper;
        this.packageAuthorityMapper = packageAuthorityMapper;
        this.efilingDocumentService = efilingDocumentService;
    }

    @Override
    public SubmitPackageResponse submitFilingPackage(
            AccountDetails accountDetails,
            FilingPackage efilingPackage,
            EfilingPaymentService paymentService) {

        if (accountDetails == null) throw new IllegalArgumentException("Account Details are required");
        if (accountDetails.getClientId() == null) throw new IllegalArgumentException("Client id is required.");
        if (efilingPackage == null) throw new IllegalArgumentException("EfilingPackage is required.");
        if (StringUtils.isBlank(efilingPackage.getApplicationCode())) throw new IllegalArgumentException("Application Type code is required.");

        logger.info("Beginning submission process");

        ServiceSession serviceSession = getServiceSession(accountDetails.getClientId().toString());

        Service createdService = createEfilingService(efilingPackage, accountDetails, serviceSession);

        //When there is no fee skip
        if (efilingPackage.getSubmissionFeeAmount() != null &&
                efilingPackage.getSubmissionFeeAmount().compareTo(BigDecimal.ZERO) > 0) {

            logger.info("Fee detected making payment");

            updatePaymentForService(
                    createdService,
                    true,
                    createPayment(paymentService, createdService, efilingPackage.getSubmissionFeeAmount(), accountDetails.getInternalClientNumber()));

        }

        ca.bc.gov.ag.csows.filing.FilingPackage csoFilingPackage = buildFilingPackage(accountDetails, efilingPackage, createdService);

        if (efilingPackage.isRushedSubmission() || efilingPackage.getRush() != null) {

            logger.info("Submission is a rush");

            csoFilingPackage.setProcRequest(buildRushedOrderRequest(accountDetails, efilingPackage.getRush(), efilingPackage.isRushedSubmission()));

        }

        determineAutoProcessingFlagFromDocuments(efilingPackage, csoFilingPackage);

        BigDecimal filingResult = filePackage(csoFilingPackage);

        updateServiceComplete(createdService);

        return SubmitPackageResponse
                .builder()
                .packageLink(MessageFormat
                        .format("{0}{1}{2}", csoProperties.getCsoBasePath(), csoProperties.getCsoPackagePath(), filingResult.toPlainString()))
                .transactionId(filingResult)
                .create();

    }

    private ca.bc.gov.ag.csows.filing.FilingPackage buildFilingPackage(AccountDetails accountDetails, FilingPackage efilingPackage, Service createdService) {

        XMLGregorianCalendar submittedDate = ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate();
        XMLGregorianCalendar computedSubmittedDate = getComputedSubmittedDate(efilingPackage.getCourt().getLocation());

        return filingPackageMapper.toFilingPackage(
                        efilingPackage,
                        accountDetails,
                        createdService.getServiceId(),
                        submittedDate,
                        buildCivilDocuments(accountDetails, efilingPackage, computedSubmittedDate),
                        buildCsoParties(accountDetails, efilingPackage),
                        buildPackageAuthorities(accountDetails));

    }

    private List<PackageAuthority> buildPackageAuthorities(AccountDetails accountDetails) {

        return Arrays.asList(packageAuthorityMapper.toPackageAuthority(accountDetails));

    }

    private List<CsoParty> buildCsoParties(AccountDetails accountDetails, FilingPackage efilingPackage) {

        List<CsoParty> csoParties = new ArrayList<>();

        Integer sequence = 0;

        for (int i = 0; i < efilingPackage.getParties().size(); i++) {
            //Due to a bug in mapstructs code generation when using expressions that refer to a specfic object
            //string formatting has to done here
            sequence++;
            csoParties.add(csoPartyMapper.toEfilingParties(sequence,
                    efilingPackage.getParties().get(i),
                    accountDetails,
                    StringUtils.capitalize(efilingPackage.getParties().get(i).getFirstName()),
                    StringUtils.upperCase(efilingPackage.getParties().get(i).getLastName())));
        }

        for (int i = 0; i < efilingPackage.getOrganizations().size(); i++) {
            sequence++;
            csoParties.add(csoPartyMapper.toEfilingOrganization(sequence,
                    efilingPackage.getOrganizations().get(i),
                    accountDetails));
        }

        return csoParties;

    }


    private List<Document> buildCivilDocuments(AccountDetails accountDetails, FilingPackage efilingPackage, XMLGregorianCalendar computedSubmittedDate) {

        List<Document> documents = new ArrayList<>();

        for (int i = 0; i < efilingPackage.getDocuments().size(); i++) {
            List<DocumentPayments> payments = Collections.singletonList(documentMapper.toEfilingDocumentPayment(efilingPackage.getDocuments().get(i), accountDetails,
                    ((efilingPackage.getDocuments().get(i).getStatutoryFeeAmount() == null || efilingPackage.getDocuments().get(i).getStatutoryFeeAmount().equals(BigDecimal.ZERO))
                            ? Keys.NOT_REQUIRED_PAYMENT_STATUS_CD : Keys.NOT_PROCESSED_PAYMENT_STATUS_CD)));
            List<Milestones> milestones = Arrays.asList(documentMapper.toActualSubmittedDate(accountDetails),
                    documentMapper.toComputedSubmittedDate(accountDetails, computedSubmittedDate));
            List<DocumentStatuses> statuses = Collections.singletonList(documentMapper.toEfilingDocumentStatus(efilingPackage.getDocuments().get(i), accountDetails));


            documents.add(documentMapper.toEfilingDocument(
                    i + 1,
                    efilingPackage.getDocuments().get(i),
                    accountDetails,
                    efilingPackage,
                    csoProperties.getFileServerHost(),
                    milestones,
                    payments,
                    statuses,
                    setDocumentId(efilingPackage.getDocuments().get(i).getActionDocument())
            ));
        }

        return documents;

    }

    private RushOrderRequest buildRushedOrderRequest(AccountDetails accountDetails, RushProcessing rushProcessing, Boolean requiredRush) {

        logger.info("build rush processing object");

        RushOrderRequest processRequest = new RushOrderRequest();
        processRequest.setEntDtm(DateUtils.getCurrentXmlDate());
        processRequest.setContactFirstGivenNm((rushProcessing != null ? rushProcessing.getFirstName() : null));
        processRequest.setContactSurnameNm((rushProcessing != null ? rushProcessing.getLastName() : null));
        processRequest.setContactPhoneNo((rushProcessing != null ? rushProcessing.getPhoneNumber() : null));
        processRequest.setCtryId((rushProcessing != null ? new BigDecimal(rushProcessing.getCountryCode()) : null));
        processRequest.setContactEmailTxt((rushProcessing != null ? rushProcessing.getEmail() : null));
        processRequest.setEntUserId(accountDetails.getClientId().toString());
        if (rushProcessing != null && !StringUtils.isBlank(rushProcessing.getCourtDate())) {
            try {
                processRequest.setRequestDt(DateUtils.getXmlDate(DateTime.parse(rushProcessing.getCourtDate())));
            } catch (DatatypeConfigurationException e) {
                logger.error("Court date is invalid");
            }
        } else {
            processRequest.setRequestDt(DateUtils.getCurrentXmlDate());
        }
        RushOrderRequestItem rushOrderRequestItem = new RushOrderRequestItem();
        rushOrderRequestItem.setEntDtm(DateUtils.getCurrentXmlDate());
        rushOrderRequestItem.setEntUserId(accountDetails.getClientId().toString());
        rushOrderRequestItem.setRushFilingReasonTxt((rushProcessing != null ? rushProcessing.getReason() : null));
        rushOrderRequestItem.setProcessReasonCd((rushProcessing != null ? RUSH_TYPES.get(rushProcessing.getRushType().toUpperCase()): RUSH_TYPES.get(RUSH_PROCESS_REASON_CD)));
        rushOrderRequestItem.getItemStatuses().add(getProcessItemStatusRequest(accountDetails));

        if (requiredRush) {
            rushOrderRequestItem.getItemStatuses().add(getProcessItemStatusApproved(accountDetails));
        }

        if (rushProcessing != null) {
            rushOrderRequestItem.getSupportDocs().addAll(buildSupportingDocuments(accountDetails, rushProcessing.getSupportingDocuments()));
        }

        processRequest.setItem(rushOrderRequestItem);
        return processRequest;

    }

    private List<ProcessSupportDocument> buildSupportingDocuments(AccountDetails accountDetails, List<ca.bc.gov.open.jag.efilingcommons.model.Document> documents) {

        List<ProcessSupportDocument> supportDocuments = new ArrayList<>();
        for (int i = 0; i < documents.size(); i++) {
            supportDocuments.add(documentMapper.toEfilingRushProcessingDocument(i+1,
                    documents.get(i),
                    accountDetails,
                    csoProperties.getFileServerHost()));
        }

        return supportDocuments;

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

    private ServiceSession getServiceSession(String clientId) {

        logger.info("Getting session");

        try {
            UserSession userSession = serviceFacadeBean.createUserSession(clientId);
            return serviceFacadeBean.createServiceSession(userSession, "request");
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while getting user session", e.getCause());
        }

    }

    private Service createEfilingService(FilingPackage efilingPackage, AccountDetails accountDetails, ServiceSession serviceSession) {

        logger.info("Creating service");

        Service serviceToCreate = serviceMapper.toCreateService(efilingPackage, accountDetails, serviceSession);

        try {
            return serviceFacadeBean.addService(serviceToCreate);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while creating efiling service", e.getCause());
        }

    }

    private void updatePaymentForService(Service service, Boolean feePaid, FinancialTransaction financialTransaction) {

        logger.info("Update payment");

        service.setFeePaidYn(feePaid);
        service.getTransactions().add(financialTransaction);

        try {
            serviceFacadeBean.updateService(service);
        } catch (ca.bc.gov.ag.csows.services.NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while updating payment on service", e.getCause());
        }

    }

    private FinancialTransaction createPayment(EfilingPaymentService paymentService, Service service, BigDecimal submissionFeeAmount, String internalClientNumber) {

        logger.info("Create payment");

        EfilingPayment efilingPayment = new EfilingPayment(service.getServiceId(), submissionFeeAmount, generateInvoiceNumber(Keys.INVOICE_PREFIX), internalClientNumber);
        PaymentTransaction payment = paymentService.makePayment(efilingPayment);
        return financialTransactionMapper.toTransaction(payment, service);

    }

    private BigDecimal filePackage(ca.bc.gov.ag.csows.filing.FilingPackage csoFilingPackage) {

        logger.info("Submit filing");

        try {
            return filingFacadeBean.submitFiling(csoFilingPackage);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingSubmissionServiceException("Exception while filing package", e.getCause());
        }

    }

    private void updateServiceComplete(Service service) {

        logger.info("Update service");

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

    private void determineAutoProcessingFlagFromDocuments(FilingPackage efilingPackage, ca.bc.gov.ag.csows.filing.FilingPackage csoFilingPackage) {

        logger.info("Determining whether auto processing flag needs to be set");

        String courtClass = efilingPackage.getCourt().getCourtClass();
        String courtLevel = efilingPackage.getCourt().getLevel();
        List<DocumentTypeDetails> documentTypeDetailsList = efilingDocumentService.getDocumentTypes(courtLevel, courtClass, Keys.DEFAULT_DIVISION);

        List<Document> documents = csoFilingPackage.getDocuments();
        for(Document document : documents) {
            String documentTypeCd = document.getDocumentTypeCd();
            for(DocumentTypeDetails documentTypeDetail : documentTypeDetailsList) {
                if(documentTypeDetail.getType().equals(documentTypeCd) && documentTypeDetail.isAutoProcessing()) {
                    csoFilingPackage.setAutomatedProcessYn(true);
                    //Set document processing
                    document.setFilingProcessCd(AUTO_PROCESSING_STATE);
                    csoFilingPackage.setDelayProcessing(determineDelayProcessing(document));
                } else {
                    document.setFilingProcessCd(MANUAL_PROCESSING_STATE);
                }
            }
        }

    }

    private Boolean determineDelayProcessing(Document document) {

        List<Milestones> milestones = document.getMilestones();
        Calendar actualSubmittedCalendar = Calendar.getInstance();
        Calendar calculatedCalendar = Calendar.getInstance();
        for(Milestones milestone : milestones) {
            if(milestone.getMilestoneTypeCd().equals(CSO_ACTUAL_SUBMITTED_DATE)) {
                actualSubmittedCalendar.setTime(xmlGregorian2Date(milestone.getMilestoneDtm()));
            } else if (milestone.getMilestoneTypeCd().equals(CSO_CALCULATED_SUBMITTED_DATE)) {
                calculatedCalendar.setTime(xmlGregorian2Date(milestone.getMilestoneDtm()));
            }
        }

        return (actualSubmittedCalendar.get(Calendar.YEAR) != calculatedCalendar.get(Calendar.YEAR)) ||
                (actualSubmittedCalendar.get(Calendar.MONTH) != calculatedCalendar.get(Calendar.MONTH)) ||
                (actualSubmittedCalendar.get(Calendar.DATE) != calculatedCalendar.get(Calendar.DATE));

    }

    //This function will be used when cso has the valid flag available
    private Boolean validateJson(Object json) {

        if (json == null) return false;
        logger.info("Validate form data");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser(json.toString());
            mapper.readTree(parser);
            logger.info("Form data is valid");
            return true;
        } catch (Exception ex) {
            logger.info("Form data is invalid");
            return false;
        }

    }

    private BigDecimal setDocumentId(ActionDocument actionDocument) {

        if (actionDocument == null) return null;

        //Rejected documents do get the document id applied
        if (actionDocument.getStatus().equalsIgnoreCase(CSO_DOCUMENT_REJECTED)) return null;

        return actionDocument.getDocumentId();

    }

}
