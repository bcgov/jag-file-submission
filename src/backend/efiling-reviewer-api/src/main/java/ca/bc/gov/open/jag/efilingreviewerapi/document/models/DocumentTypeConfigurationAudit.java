package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;

/**
 * A model object to store a history of transactions against {@link DocumentTypeConfiguration}. Besides the object
 * itself, includes an action (INSERT, UPDATE, or DELETE) and a timestamp.
 */
public class DocumentTypeConfigurationAudit {

	@Id
	private UUID id;
	private DocumentTypeConfiguration DocumentTypeConfiguration;
	private AuditAction auditAction;
	private Date auditDate;

	public DocumentTypeConfigurationAudit() {
		id = UUID.randomUUID();
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public DocumentTypeConfiguration getDocumentTypeConfiguration() {
		return DocumentTypeConfiguration;
	}

	public void setDocumentTypeConfiguration(DocumentTypeConfiguration documentTypeConfiguration) {
		DocumentTypeConfiguration = documentTypeConfiguration;
	}

	public AuditAction getAuditAction() {
		return auditAction;
	}

	public void setAuditAction(AuditAction auditAction) {
		this.auditAction = auditAction;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

}
