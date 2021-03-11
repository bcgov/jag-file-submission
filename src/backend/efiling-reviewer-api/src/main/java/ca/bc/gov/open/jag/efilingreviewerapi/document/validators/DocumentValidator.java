package ca.bc.gov.open.jag.efilingreviewerapi.document.validators;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentValidator {
    boolean validateDocument(String documentType, MultipartFile file);
}
