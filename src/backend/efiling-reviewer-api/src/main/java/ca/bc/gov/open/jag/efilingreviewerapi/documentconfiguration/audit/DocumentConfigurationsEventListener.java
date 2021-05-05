package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.audit;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.AuditAction;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfigurationAudit;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentConfigurationAuditRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;

@Component
public class DocumentConfigurationsEventListener extends AbstractMongoEventListener<DocumentTypeConfiguration> {

	@Autowired
	private DocumentTypeConfigurationRepository documentTypeConfigurationRepository;

	@Autowired
	private DocumentConfigurationAuditRepository documentConfigurationAuditRepository;

	@Override
	public void onAfterSave(AfterSaveEvent<DocumentTypeConfiguration> event) {
		super.onAfterSave(event);

		DocumentTypeConfigurationAudit documentTypeConfigurationAudit = new DocumentTypeConfigurationAudit();
		documentTypeConfigurationAudit.setAuditDate(new Date(event.getTimestamp()));
		documentTypeConfigurationAudit.setDocumentTypeConfiguration(event.getSource());
		if (event.getSource().getVersion().intValue() == 0) {
			documentTypeConfigurationAudit.setAuditAction(AuditAction.INSERT);
		}
		else {
			documentTypeConfigurationAudit.setAuditAction(AuditAction.UPDATE);
		}

		documentConfigurationAuditRepository.save(documentTypeConfigurationAudit);
	}

	@Override
	public void onBeforeDelete(BeforeDeleteEvent<DocumentTypeConfiguration> event) {
		super.onBeforeDelete(event);
		// retrieve document we are about to delete so we can log it's deletion (event doesn't include this object, only the UUID)
		DocumentTypeConfiguration documentTypeConfiguration = documentTypeConfigurationRepository.findById((UUID)event.getDocument().get("_id")).get();

		DocumentTypeConfigurationAudit documentTypeConfigurationAudit = new DocumentTypeConfigurationAudit();
		documentTypeConfigurationAudit.setAuditAction(AuditAction.DELETE);
		documentTypeConfigurationAudit.setAuditDate(new Date(event.getTimestamp()));
		documentTypeConfigurationAudit.setDocumentTypeConfiguration(documentTypeConfiguration);

		documentConfigurationAuditRepository.save(documentTypeConfigurationAudit);
	}

}
