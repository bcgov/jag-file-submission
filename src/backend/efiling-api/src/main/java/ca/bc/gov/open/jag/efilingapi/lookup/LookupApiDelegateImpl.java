package ca.bc.gov.open.jag.efilingapi.lookup;

import ca.bc.gov.open.jag.efilingapi.api.LookupApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentTypes;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
public class LookupApiDelegateImpl implements LookupApiDelegate {
    Logger logger = LoggerFactory.getLogger(LookupApiDelegateImpl.class);

    private final DocumentStore documentStore;

    public LookupApiDelegateImpl(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }


    @Override
    public ResponseEntity<DocumentTypes> getDocumentTypes(String courtLevel, String courtClass) {

        try {
            DocumentTypes result = new DocumentTypes();
            result.setDocumentTypes(documentStore.getDocumentTypes(courtLevel, courtClass).stream()
                    .map(documentType -> toDocumentType(documentType)).collect(Collectors.toList()));
            return ResponseEntity.ok(result);
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            EfilingError response = new EfilingError();
            response.setError(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode());
            response.setMessage(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private DocumentType toDocumentType(ca.bc.gov.open.jag.efilingcommons.model.DocumentType documentType) {
        DocumentType outDocumentType = new DocumentType();
        outDocumentType.setType(documentType.getType());
        outDocumentType.setDescription(documentType.getDescription());
        return outDocumentType;
    }
}
