package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {
    @Override
    public ResponseEntity<Object> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {
        return null;
    }
}
