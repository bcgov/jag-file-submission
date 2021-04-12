package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument;

import ca.bc.gov.open.jag.efilingreviewerapi.api.RestrictedDocumentTypesApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.mappers.RestrictedDocumentTypeMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public class RestrictedDocumentApiDelegateImpl implements RestrictedDocumentTypesApiDelegate {

    private final RestrictedDocumentRepository restrictedDocumentRepository;
    private final RestrictedDocumentTypeMapper restrictedDocumentTypeMapper;

    public RestrictedDocumentApiDelegateImpl(RestrictedDocumentRepository restrictedDocumentRepository,
                                             RestrictedDocumentTypeMapper restrictedDocumentTypeMapper) {
        this.restrictedDocumentRepository = restrictedDocumentRepository;
        this.restrictedDocumentTypeMapper = restrictedDocumentTypeMapper;
    }

    @Override
    public ResponseEntity<RestrictedDocumentType> createRestrictedDocumentType(DocumentType documentType) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteRestrictedDocumentType(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<RestrictedDocumentType> getRestrictedDocumentType(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<List<RestrictedDocumentType>> getRestrictedDocumentTypes() {
        return null;
    }

    @Override
    public ResponseEntity<DocumentTypeConfiguration> updateRestrictedDocumentType(RestrictedDocumentType restrictedDocumentType) {
        return null;
    }

}
