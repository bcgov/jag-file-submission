package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.DocumentStatuses;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.status.*;
import ca.bc.gov.ag.csows.reports.Report;
import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingReviewServiceException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.RushDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CsoReviewServiceImpl implements EfilingReviewService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    private final ReportService reportService;

    private final FilingFacadeBean filingFacadeBean;

    private final FilePackageMapper filePackageMapper;

    private final CsoProperties csoProperties;

    private final RestTemplate restTemplate;

    private final EfilingLookupService lookupService;

    public CsoReviewServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean, ReportService reportService, FilingFacadeBean filingFacadeBean, FilePackageMapper filePackageMapper, CsoProperties csoProperties, RestTemplate restTemplate, EfilingLookupService lookupService) {

        this.filingStatusFacadeBean = filingStatusFacadeBean;
        this.reportService = reportService;
        this.filingFacadeBean = filingFacadeBean;
        this.filePackageMapper = filePackageMapper;
        this.csoProperties = csoProperties;
        this.restTemplate = restTemplate;
        this.lookupService = lookupService;

    }

    @Override
    public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {

        try {

            logger.info("Calling soap findStatusBySearchCriteria by client id and package service ");
            //Is account ID required? It is the variable after client ID
            FilingStatus filingStatus = filingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, null, null, null, filingPackageRequest.getPackageNo(), filingPackageRequest.getClientId(), filingPackageRequest.getAccountId(), null, null, null, null, BigDecimal.ONE, null, null, null);

            if (filingStatus.getFilePackages() == null || filingStatus.getFilePackages().isEmpty()) return Optional.empty();

            return Optional.of(filePackageMapper.toFilingPackage(
                    filingStatus.getFilePackages().get(0),
                    getViewAllPackagesUrl(),
                    filingStatus.getFilePackages().get(0).getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.INDIVIDUAL_ROLE_TYPE_CD))
                            .map(filePackageMapper::toParty)
                            .collect(Collectors.toList()),
                    filingStatus.getFilePackages().get(0).getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.ORGANIZATION_ROLE_TYPE_CD))
                            .map(filePackageMapper::toOrganization)
                            .collect(Collectors.toList()),
                    getRushOrderItem(filingStatus.getFilePackages().get(0)),
                    getCountryDescription(filingStatus.getFilePackages().get(0)),
                    getPackageStatus(filingStatus.getFilePackages().get(0).getFiles())));

        } catch (NestedEjbException_Exception e) {

            throw new EfilingStatusServiceException("Exception while finding status", e.getCause());

        }

    }

    @Override
    public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
        try {

            logger.info("Calling soap findStatusBySearchCriteria by client id service ");

            DateTime endDate = DateTime.now();
            DateTime startDate = endDate.minusYears(1);
            //Is account ID required? It is the variable after client ID
            FilingStatus filingStatus = filingStatusFacadeBean
                    .findStatusBySearchCriteria(null, null, null, DateUtils.getXmlDate(startDate), DateUtils.getXmlDate(endDate), null , null, filingPackageRequest.getClientId(), filingPackageRequest.getAccountId(), null, null, null, null, BigDecimal.valueOf(100), null, filingPackageRequest.getParentApplication(), null);

            if (filingStatus.getFilePackages().isEmpty()) return new ArrayList<>();

            return filingStatus.getFilePackages().stream().map(filePackage -> filePackageMapper.toFilingPackage(filePackage,
                    getViewAllPackagesUrl(),
                    filePackage.getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.INDIVIDUAL_ROLE_TYPE_CD))
                            .map(filePackageMapper::toParty)
                            .collect(Collectors.toList()),
                    filePackage.getParties().stream()
                            .filter(individual -> individual.getPartyTypeCd().equalsIgnoreCase(Keys.ORGANIZATION_ROLE_TYPE_CD))
                            .map(filePackageMapper::toOrganization)
                            .collect(Collectors.toList()),
                    getRushOrderItem(filingStatus.getFilePackages().get(0)),
                    getCountryDescription(filingStatus.getFilePackages().get(0)),
                    getPackageStatus(filingStatus.getFilePackages().get(0).getFiles()))).collect(Collectors.toList());

        } catch (NestedEjbException_Exception | DatatypeConfigurationException e) {

            throw new EfilingStatusServiceException("Exception while finding status list", e.getCause());

        }
    }

    @Override
    public Optional<byte[]> getReport(ReportRequest reportRequest) {
        String reportName;
        String parameterName;
        switch (reportRequest.getReport()) {
            case SUBMISSION_SHEET:
                reportName = Keys.SUBMISSION_REPORT_NAME;
                parameterName = Keys.SUBMISSION_REPORT_PARAMETER;
                break;
            case PAYMENT_RECEIPT:
                reportName = Keys.RECEIPT_REPORT_NAME;
                parameterName = Keys.PARAM_REPORT_PARAMETER;
                break;
            case REGISTRY_NOTICE:
                reportName = Keys.REGISTRY_NOTICE_NAME;
                parameterName = Keys.PARAM_REPORT_PARAMETER;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + reportRequest.getReport());
        }

        logger.info("Calling soap to retrieve {} report ", reportName);

        Report report = new Report();
        report.setName(reportName);
        report.getParameters().addAll(Arrays.asList(parameterName, reportRequest.getPackageId().toPlainString()));

        byte[] result = reportService.runReport(report);

        if (result == null || result.length == 0) return Optional.empty();

        return Optional.of(result);

    }

    @Override
    public Optional<byte[]> getSubmittedDocument(BigDecimal documentIdentifier) {

        String url = "";

        try {
            url = filingFacadeBean.getActiveDocumentURL(documentIdentifier);
        } catch (ca.bc.gov.ag.csows.filing.NestedEjbException_Exception e) {
            logger.error("Error in [updateDocumentStatus] call");
            throw new EfilingReviewServiceException("Failed to retrieved document", e.getCause());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class, entity);

        return Optional.of(response.getBody());

    }

    @Override
    public void deleteSubmittedDocument(DeleteSubmissionDocumentRequest deleteSubmissionDocumentRequest) {
        try {

            logger.debug("Calling soap [updateDocumentStatus] ");

            DocumentStatuses documentStatuses = new DocumentStatuses();
            documentStatuses.setDocumentId(new BigDecimal(deleteSubmissionDocumentRequest.getDocumentId()));
            documentStatuses.setEntDtm(DateUtils.getCurrentXmlDate());
            documentStatuses.setDocumentStatusTypeCd(ca.bc.gov.open.jag.efilingcommons.Keys.WITHDRAWN_STATUS_CD);
            documentStatuses.setCreatedByPartId(deleteSubmissionDocumentRequest.getClientId());
            documentStatuses.setEntUserId(deleteSubmissionDocumentRequest.getClientId().toEngineeringString());
            documentStatuses.setUpdUserId(deleteSubmissionDocumentRequest.getClientId().toEngineeringString());
            documentStatuses.setStatusDtm(DateUtils.getCurrentXmlDate());

            filingFacadeBean.updateDocumentStatus(documentStatuses);

            logger.info(" [updateDocumentStatus] Successful  ");

        } catch (ca.bc.gov.ag.csows.filing.NestedEjbException_Exception e) {

            logger.error("Error in [updateDocumentStatus] call");

            throw  new EfilingReviewServiceException("Failed to updateDocumentStatus", e.getCause());

        }

        try {

            logger.debug("Calling soap [inactivateReferrals] ");

            filingFacadeBean.inactivateReferrals(deleteSubmissionDocumentRequest.getClientId(), DateUtils.getCurrentXmlDate(), deleteSubmissionDocumentRequest.getDocumentId());

            logger.info(" [updateDocumentStatus] Successful ");

        } catch (ca.bc.gov.ag.csows.filing.NestedEjbException_Exception e) {

            logger.error("Error in [inactivateReferrals] call");

            throw  new EfilingReviewServiceException("Failed in inactivateReferrals", e.getCause());

        }

        try {

            logger.debug("Calling soap [removePackageParties] ");

            filingFacadeBean.removePackageParties(deleteSubmissionDocumentRequest.getPackageNo());

            logger.info(" [updateDocumentStatus]  Successful ");

        } catch (ca.bc.gov.ag.csows.filing.NestedEjbException_Exception e) {

            logger.error("Error in [removePackageParties] call");

            throw  new EfilingReviewServiceException("Failed in removePackageParties", e.getCause());

        }

    }

    @Override
    public Optional<byte[]> getRushDocument(RushDocumentRequest rushDocumentRequest) {

        logger.info("Calling soap service to retrieve rush document");

        String url = "";

        try {
            url = filingFacadeBean.getActiveSuppDocURL(rushDocumentRequest.getProcReqId(), rushDocumentRequest.getProcItemSeqNo(), rushDocumentRequest.getDocSeqNo());
        } catch (ca.bc.gov.ag.csows.filing.NestedEjbException_Exception e) {
            logger.error("Error in [getActiveSuppDocURL] call");
            throw new EfilingReviewServiceException("Failed to retrieved document", e.getCause());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class, entity);

        return Optional.of(response.getBody());

    }

    private String getViewAllPackagesUrl() {
        return MessageFormat.format("{0}/{1}", csoProperties.getCsoBasePath(),  Keys.VIEW_ALL_PACKAGE_SUBPATH);
    }

    private RushOrderRequestItem getRushOrderItem(FilePackage filePackage) {
        if (filePackage.getProcRequest() == null) return new RushOrderRequestItem();

        return filePackage.getProcRequest().getItem();

    }

    private String getCountryDescription(FilePackage filePackage) {
        if (filePackage.getProcRequest() == null || filePackage.getProcRequest().getCtryId() == null) return null;

        Optional<LookupItem> countryItem = lookupService.getCountries().stream()
                .filter(country -> country.getCode().equals(filePackage.getProcRequest().getCtryId().toEngineeringString()))
                .findFirst();

        return countryItem.map(LookupItem::getDescription).orElse(null);

    }

    private String getPackageStatus(List<File> documents) {

        boolean pending = false;

        if (documents != null && !documents.isEmpty()) {
            //Package status is calculated based on document status
            for (File document : documents) {

                if (document.getStatus().equalsIgnoreCase(Keys.CSO_DOCUMENT_REJECTED) ||
                    document.getStatus().equalsIgnoreCase(Keys.CSO_DOCUMENT_COURTESY_CORRECTED))
                    return Keys.PACKAGE_STATUS_ACTION_REQUIRED;

                if (document.getStatus().equalsIgnoreCase(Keys.CSO_DOCUMENT_RE_SUBMITTED) ||
                    document.getStatus().equalsIgnoreCase(Keys.CSO_DOCUMENT_REFERRED) ||
                    document.getStatus().equalsIgnoreCase(Keys.CSO_DOCUMENT_SUBMITTED))
                    pending = true;
            }

        }

        if (pending) return Keys.PACKAGE_STATUS_PENDING;

        return Keys.PACKAGE_STATUS_COMPLETE;

    }

}
