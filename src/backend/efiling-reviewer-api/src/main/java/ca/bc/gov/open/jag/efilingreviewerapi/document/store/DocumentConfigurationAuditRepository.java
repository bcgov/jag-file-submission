package ca.bc.gov.open.jag.efilingreviewerapi.document.store;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfigurationAudit;

public interface DocumentConfigurationAuditRepository extends MongoRepository<DocumentTypeConfigurationAudit, UUID> {

}
