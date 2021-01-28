package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.api.FilingpackagesApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

@Service
public class FilingpackageApiDelegateImpl implements FilingpackagesApiDelegate {

    private final FilingPackageService filingPackageService;

    public FilingpackageApiDelegateImpl(FilingPackageService filingPackageService) {
        this.filingPackageService = filingPackageService;
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getFilingPackage(BigDecimal packageIdentifier) {

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

        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        Optional<byte[]> result = filingPackageService.getSubmissionSheet(packageIdentifier);

        return result.<ResponseEntity<Resource>>map(bytes -> ResponseEntity.ok(new ByteArrayResource(bytes))).orElseGet(() -> new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.SUBMISSION_SHEET_NOT_FOUND).create(), HttpStatus.NOT_FOUND));

    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<Resource> getSubmittedDocument(String documentIdentifier) {
        Optional<String> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(new ByteArrayResource("".getBytes()));

    }
}
