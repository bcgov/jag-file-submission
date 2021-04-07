package ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentTypeConfigurationsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfigurationRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration.mappers.DocumentTypeConfigurationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentConfigurationsApiDelegateImpl implements DocumentTypeConfigurationsApiDelegate {

    private final DocumentTypeConfigurationRepository documentTypeConfigurationRepository;
    private final DocumentTypeConfigurationMapper documentTypeConfigurationMapper;


    public DocumentConfigurationsApiDelegateImpl(DocumentTypeConfigurationRepository documentTypeConfigurationRepository, DocumentTypeConfigurationMapper documentTypeConfigurationMapper) {
        this.documentTypeConfigurationRepository = documentTypeConfigurationRepository;
        this.documentTypeConfigurationMapper = documentTypeConfigurationMapper;
    }

    @Override
    public ResponseEntity<List<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration>> getDocumentConfigurations() {

        List<DocumentTypeConfiguration> documentTypeConfigurations = documentTypeConfigurationRepository.findAll();

        return ResponseEntity.ok(documentTypeConfigurations.stream()
                .map(x -> documentTypeConfigurationMapper.toDocumentTypeConfiguration(x))
        .collect(Collectors.toList()));

    }

    @Override
    public ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration> createDocumentTypeConfiguration(DocumentTypeConfigurationRequest documentTypeConfigurationRequest) {

        if(documentTypeConfigurationRepository.findByDocumentType(documentTypeConfigurationRequest.getDocumentType()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                .builder()
                .documentType(documentTypeConfigurationRequest.getDocumentType())
                .documentConfig((LinkedHashMap<String, Object>) documentTypeConfigurationRequest.getDocumentConfig())
                .create();

        DocumentTypeConfiguration savedDocument = documentTypeConfigurationRepository.save(documentTypeConfiguration);

        return ResponseEntity.ok(documentTypeConfigurationMapper.toDocumentTypeConfiguration(savedDocument));

    }
}
