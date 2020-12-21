package ca.bc.gov.open.jag.efilingapi.filepackage;

import ca.bc.gov.open.jag.efilingapi.api.FilepackageApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filepackage.service.FilePackageService;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class FilepackageApiDelegateImpl implements FilepackageApiDelegate {

    private final FilePackageService filePackageService;

    public FilepackageApiDelegateImpl(FilePackageService filePackageService) {
        this.filePackageService = filePackageService;
    }

    @Override
    public ResponseEntity<FilingPackage> getFilePackage(BigDecimal clientId, BigDecimal packageNumber) {
        return ResponseEntity.ok(new FilingPackage());
    }
}
