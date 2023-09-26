package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.api.DocumentsApi;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtClassification;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLevel;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingapi.error.DocumentTypeException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentApiDelegateImpl implements DocumentsApi {
    Logger logger = LoggerFactory.getLogger(DocumentApiDelegateImpl.class);

    private static final String DOCUMENT_TYPE_ERROR = "Error while retrieving documents";
    private final DocumentStore documentStore;

    public DocumentApiDelegateImpl(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @Override
    // FIXME: replace with @PreAuthorize
    // @RolesAllowed({Keys.EFILING_USER_ROLE, Keys.EFILING_CLIENT_ROLE})
    public ResponseEntity<List<DocumentType>> getDocumentTypes(@NotNull @Valid CourtLevel courtLevel, @NotNull @Valid CourtClassification courtClassification) {
        try {
            return ResponseEntity.ok(documentStore.getDocumentTypes(courtLevel.getValue(), courtClassification.getValue()).stream()
                    .map(this::toDocumentType).collect(Collectors.toList()));
        } catch (EfilingDocumentServiceException e) {
            logger.warn(e.getMessage(), e);
            throw new DocumentTypeException(DOCUMENT_TYPE_ERROR);
        }
    }

    private DocumentType toDocumentType(DocumentTypeDetails documentTypeDetails) {
        DocumentType outDocumentType = new DocumentType();
        outDocumentType.setType(documentTypeDetails.getType());
        outDocumentType.setDescription(documentTypeDetails.getDescription());
        return outDocumentType;
    }
}
