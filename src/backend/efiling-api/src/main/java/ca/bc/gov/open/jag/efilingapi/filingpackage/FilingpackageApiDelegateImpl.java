package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.api.FilingpackagesApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
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
import java.util.Optional;

@Service
public class FilingpackageApiDelegateImpl implements FilingpackagesApiDelegate {

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

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);


        Optional<FilingPackage> result = filingPackageService.getCSOFilingPackage(universalId.get(), packageIdentifier);

        return result.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.FILING_PACKAGE_NOT_FOUND).create(), HttpStatus.NOT_FOUND));
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmissionSheet(BigDecimal packageIdentifier) {

        logger.info("get submission sheet request received");

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        Optional<Resource> result = filingPackageService.getSubmissionSheet(packageIdentifier);

        return result.<ResponseEntity<Resource>>map(bytes -> ResponseEntity.ok(result.get())).orElseGet(() -> new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.SUBMISSION_SHEET_NOT_FOUND).create(), HttpStatus.NOT_FOUND));

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getPaymentReceipt(BigDecimal packageIdentifier) {

        logger.info("get payment receipt request received");

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        Optional<Resource> result = filingPackageService.getPaymentReceipt(packageIdentifier);

        if(!result.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.PAYMENT_RECEIPT_NOT_FOUND).create(), HttpStatus.NOT_FOUND);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, MessageFormat.format("attachment; filename={0}", Keys.EFILING_PAYMENT_RECEIPT_FILENAME));

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

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        Optional<SubmittedDocument> result = filingPackageService.getSubmittedDocument(universalId.get(), packageIdentifier, documentIdentifier);

        if(!result.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.DOCUMENT_NOT_FOUND).create(), HttpStatus.NOT_FOUND);

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

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

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
