package ca.bc.gov.open.jag.efilingreviewerapi.document.store;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.RestrictedDocumentType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface RestrictedDocumentRepository extends MongoRepository<RestrictedDocumentType, UUID> {

    boolean existsByDocumentTypeDescription(String documentTypeDescription);

    boolean existsByDocumentType(String documentType);

}
