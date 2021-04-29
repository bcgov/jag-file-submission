package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.api.FilingpackagesApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.error.FilingPackageNotFoundException;
import ca.bc.gov.open.jag.efilingapi.error.MissingUniversalIdException;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportsTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class FilingpackageApiDelegateImpl implements FilingpackagesApiDelegate {

    private static final String FILING_PACKAGE_NOT_FOUND = "Requested filing package was not found.";
    private static final String MISSING_UNIVERSAL_ID = "universal-id claim missing in jwt token.";

    private final FilingPackageService filingPackageService;
    Logger logger = LoggerFactory.getLogger(FilingpackageApiDelegateImpl.class);

    public FilingpackageApiDelegateImpl(FilingPackageService filingPackageService) {
        this.filingPackageService = filingPackageService;
    }



    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getFilingPackage(BigDecimal packageIdentifier) {

        logger.info("get filing package request received");

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent())
            throw new MissingUniversalIdException(MISSING_UNIVERSAL_ID);

        Optional<FilingPackage> result = filingPackageService.getCSOFilingPackage(universalId.get(), packageIdentifier);

        return result.map(ResponseEntity::ok).orElseThrow(() -> new FilingPackageNotFoundException(FILING_PACKAGE_NOT_FOUND));
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<List<FilingPackage>> getFilingPackages(String parentApplication) {

        logger.info("get filing packages request received");

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent())
            throw new MissingUniversalIdException(MISSING_UNIVERSAL_ID);

        Optional<List<FilingPackage>> result = filingPackageService.getFilingPackages(universalId.get(), parentApplication);

        return result.map(ResponseEntity::ok).orElseThrow(() -> new FilingPackageNotFoundException(FILING_PACKAGE_NOT_FOUND));
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmissionSheet(BigDecimal packageIdentifier) {

        logger.info("get submission sheet request received");

        return getReport(ReportRequest.builder()
                .report(ReportsTypes.SUBMISSION_SHEET)
                .packageId(packageIdentifier)
                .fileName(Keys.EFILING_SUBMISSION_SHEET_FILENAME).create());

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getPaymentReceipt(BigDecimal packageIdentifier) {

        logger.info("get payment receipt request received");

        return getReport(ReportRequest.builder()
                        .report(ReportsTypes.PAYMENT_RECEIPT)
                        .packageId(packageIdentifier)
                        .fileName(Keys.EFILING_PAYMENT_RECEIPT_FILENAME).create());

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getRegistryNotice(BigDecimal packageIdentifier) {

        logger.info("get registry notice request received");

        return getReport(ReportRequest.builder()
                .report(ReportsTypes.REGISTRY_NOTICE)
                .packageId(packageIdentifier)
                .fileName(Keys.EFILING_REGISTRY_NOTICE_FILENAME).create());

    }

    private ResponseEntity<Resource> getReport(ReportRequest reportRequest) {
        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Optional<Resource> result = filingPackageService.getReport(reportRequest);

        if(!result.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, MessageFormat.format("attachment; filename={0}", result.get().getFilename()));

        return ResponseEntity.ok()
                .headers(header)
                .body(result.get());

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmittedDocument(BigDecimal packageIdentifier,
                                                         BigDecimal documentIdentifier) {

        logger.info("get document request received");

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Optional<SubmittedDocument> result = filingPackageService.getSubmittedDocument(universalId.get(), packageIdentifier, documentIdentifier);

        if(!result.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, MessageFormat.format("attachment; filename={0}",result.get().getName()));

        return ResponseEntity.ok()
                .headers(header)
                .body(result.get().getData());

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Void> deleteSubmittedDocument(BigDecimal packageIdentifier, String documentIdentifier) {

        logger.info("delete document request received");

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent())
            throw new MissingUniversalIdException(MISSING_UNIVERSAL_ID);

        try {
            filingPackageService.deleteSubmittedDocument(universalId.get(), packageIdentifier, documentIdentifier);
            return ResponseEntity.ok(null);
        } catch (EfilingAccountServiceException e) {
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DELETE_DOCUMENT_ERROR).create(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(
                    EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DELETE_DOCUMENT_ERROR).create(), HttpStatus.BAD_REQUEST);
        }
    }

}
