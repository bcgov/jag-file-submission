package ca.bc.gov.open.jag.efilingapi.filingpackage;

import ca.bc.gov.open.jag.efilingapi.api.FilepackageApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.error.EfilingErrorBuilder;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class FilepackageApiDelegateImpl implements FilepackageApiDelegate {

    private final FilingPackageService filingPackageService;

    public FilepackageApiDelegateImpl(FilingPackageService filingPackageService) {
        this.filingPackageService = filingPackageService;
    }

    @Override
    @RolesAllowed("efiling-user")
    public ResponseEntity<FilingPackage> getFilePackage(BigDecimal packageIdentifier) {

        Optional<UUID> universalId = SecurityUtils.getUniversalIdFromContext();

        if(!universalId.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.MISSING_UNIVERSAL_ID).create(), HttpStatus.FORBIDDEN);


        Optional<FilingPackage> result = filingPackageService.getCSOFilingPackage(universalId.get(), packageIdentifier);

        if (!result.isPresent()) return new ResponseEntity(
                EfilingErrorBuilder.builder().errorResponse(ErrorResponse.FILING_PACKAGE_NOT_FOUND).create(), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(result.get());
    }
}
