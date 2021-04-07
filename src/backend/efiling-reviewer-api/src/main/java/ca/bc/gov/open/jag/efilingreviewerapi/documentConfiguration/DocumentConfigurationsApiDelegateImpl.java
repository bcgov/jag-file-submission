package ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentTypeConfigurationsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration.mappers.DocumentTypeConfigurationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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



}
