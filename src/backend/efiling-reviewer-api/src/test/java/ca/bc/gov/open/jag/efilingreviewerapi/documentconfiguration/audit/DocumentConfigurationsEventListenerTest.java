package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.audit;

import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.AuditAction;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfigurationAudit;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentConfigurationAuditRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentConfigurationsEventListenerTest {

	@Mock
	private DocumentTypeConfigurationRepository documentTypeConfigurationRepository;
	@Mock
	private DocumentConfigurationAuditRepository documentConfigurationAuditRepository;
	@InjectMocks
	private DocumentConfigurationsEventListener documentConfigurationsEventListener;

	@BeforeEach
	public void beforeAll() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void onAfterSave_INSERT() {
		// When saving a documentTypeConfiguration the first time, we expect to see
		// documentConfigurationAuditRepository called with a new DocumentTypeConfigurationAudit audit record of type INSERT

		ArgumentMatcher<DocumentTypeConfigurationAudit> argMatcher = new ArgMatcher(AuditAction.INSERT);

		// document to be saved
		DocumentTypeConfiguration documentTypeConfiguration = new DocumentTypeConfiguration();
		documentTypeConfiguration.setVersion(Integer.valueOf(0)); // denotes the 1st version of this record

		// expected audit record
		DocumentTypeConfigurationAudit expectedAudit = new DocumentTypeConfigurationAudit();
		expectedAudit.setAuditAction(AuditAction.INSERT);
		expectedAudit.setDocumentTypeConfiguration(documentTypeConfiguration);

		Mockito.when(documentConfigurationAuditRepository.save(Mockito.argThat(argMatcher))).thenReturn(expectedAudit);

		// call the event
		AfterSaveEvent<DocumentTypeConfiguration> event = new AfterSaveEvent<DocumentTypeConfiguration>(
				documentTypeConfiguration, new Document(), "");
		documentConfigurationsEventListener.onAfterSave(event);

		// assert the save occurred with a new INSERT audit record
		Mockito.verify(documentConfigurationAuditRepository, Mockito.atLeastOnce()).save(Mockito.argThat(argMatcher));
	}

	@Test
	public void onAfterSave_UPDATE() {
		// When saving a documentTypeConfiguration the second time (an update), we expect to see
		// documentConfigurationAuditRepository called with a new DocumentTypeConfigurationAudit audit record of type UPDATE

		ArgumentMatcher<DocumentTypeConfigurationAudit> argMatcher = new ArgMatcher(AuditAction.UPDATE);

		// document to be updated
		DocumentTypeConfiguration documentTypeConfiguration = new DocumentTypeConfiguration();
		documentTypeConfiguration.setVersion(Integer.valueOf(1)); // denotes the 2nd version of this record, an update

		// expected audit record
		DocumentTypeConfigurationAudit expectedAudit = new DocumentTypeConfigurationAudit();
		expectedAudit.setAuditAction(AuditAction.UPDATE);
		expectedAudit.setDocumentTypeConfiguration(documentTypeConfiguration);

		Mockito.when(documentConfigurationAuditRepository.save(Mockito.argThat(argMatcher))).thenReturn(expectedAudit);

		// call the event
		AfterSaveEvent<DocumentTypeConfiguration> event = new AfterSaveEvent<DocumentTypeConfiguration>(
				documentTypeConfiguration, new Document(), "");
		documentConfigurationsEventListener.onAfterSave(event);

		// assert the save occurred with a new UPDATE audit record
		Mockito.verify(documentConfigurationAuditRepository, Mockito.atLeastOnce()).save(Mockito.argThat(argMatcher));
	}

	@Test
	public void onBeforeDelete() {
		// When deleting a documentTypeConfiguration, we expect to see
		// documentConfigurationAuditRepository called with a new DocumentTypeConfigurationAudit audit record of type DELETE

		ArgumentMatcher<DocumentTypeConfigurationAudit> argMatcher = new ArgMatcher(AuditAction.DELETE);

		// document to be deleted
		DocumentTypeConfiguration documentTypeConfiguration = new DocumentTypeConfiguration();
		documentTypeConfiguration.setId(UUID.randomUUID());
		documentTypeConfiguration.setVersion(Integer.valueOf(0));

		Document document = new Document("_id", documentTypeConfiguration.getId());

		// expected audit record
		DocumentTypeConfigurationAudit expectedAudit = new DocumentTypeConfigurationAudit();
		expectedAudit.setAuditAction(AuditAction.DELETE);
		expectedAudit.setDocumentTypeConfiguration(documentTypeConfiguration);

		Mockito.when(documentTypeConfigurationRepository.findById(documentTypeConfiguration.getId()))
				.thenReturn(Optional.of(documentTypeConfiguration));
		Mockito.when(documentConfigurationAuditRepository.save(Mockito.argThat(argMatcher))).thenReturn(expectedAudit);

		// call the event
		BeforeDeleteEvent<DocumentTypeConfiguration> event = new BeforeDeleteEvent<DocumentTypeConfiguration>(document,
				DocumentTypeConfiguration.class, "");
		documentConfigurationsEventListener.onBeforeDelete(event);

		// assert the save occurred with a new DELETE audit record
		Mockito.verify(documentConfigurationAuditRepository, Mockito.atLeastOnce()).save(Mockito.argThat(argMatcher));
	}

	private class ArgMatcher implements ArgumentMatcher<DocumentTypeConfigurationAudit> {

		private AuditAction auditAction;

		public ArgMatcher(AuditAction auditAction) {
			this.auditAction = auditAction;
		}

		@Override
		public boolean matches(DocumentTypeConfigurationAudit argument) {
			return auditAction.equals(argument.getAuditAction());
		}

	}

}
