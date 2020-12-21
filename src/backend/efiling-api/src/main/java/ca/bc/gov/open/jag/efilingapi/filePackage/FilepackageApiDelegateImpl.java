package ca.bc.gov.open.jag.efilingapi.filePackage;

import ca.bc.gov.open.jag.efilingapi.api.FilepackageApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class FilepackageApiDelegateImpl implements FilepackageApiDelegate {
    @Override
    public ResponseEntity<FilingPackage> getFilePackage(BigDecimal packageIdentifier) {
        return ResponseEntity.ok(new FilingPackage());
    }
}
