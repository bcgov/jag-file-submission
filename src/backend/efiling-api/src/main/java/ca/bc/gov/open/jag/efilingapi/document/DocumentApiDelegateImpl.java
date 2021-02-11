package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@Service
public class DocumentApiDelegateImpl implements DocumentsApiDelegate {
    Logger logger = LoggerFactory.getLogger(DocumentApiDelegateImpl.class);

    private final DocumentStore documentStore;

    public DocumentApiDelegateImpl(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @Override
    @RolesAllowed({Keys.EFILING_USER_ROLE, Keys.EFILING_CLIENT_ROLE})
    public ResponseEntity<DocumentTypes> getDocumentTypes(@NotNull @Valid CourtLevel courtLevel, @NotNull @Valid CourtClassification courtClassification) {
        try {
            DocumentTypes result = new DocumentTypes();
            result.setDocumentTypes(documentStore.getDocumentTypes(courtLevel.getValue(), courtClassification.getValue()).stream()
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
