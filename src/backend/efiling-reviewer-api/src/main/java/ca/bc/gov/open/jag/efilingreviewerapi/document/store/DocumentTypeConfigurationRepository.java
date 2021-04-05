package ca.bc.gov.open.jag.efilingreviewerapi.document.store;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DocumentTypeConfigurationRepository extends MongoRepository<DocumentTypeConfiguration, UUID> {

    DocumentTypeConfiguration findByDocumentType(String documentType);

}
