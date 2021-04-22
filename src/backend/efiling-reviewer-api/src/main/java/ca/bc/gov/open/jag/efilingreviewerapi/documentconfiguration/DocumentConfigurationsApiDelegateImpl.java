package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration;

import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentTypeConfigurationsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfigurationRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentTypeConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public ResponseEntity<List<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration>> getDocumentConfigurations(String documentType) {

        List<DocumentTypeConfiguration> documentTypeConfigurations;
        if (StringUtils.isBlank(documentType)) {
            documentTypeConfigurations = documentTypeConfigurationRepository.findAll();
        } else {
            documentTypeConfigurations = Collections.singletonList(documentTypeConfigurationRepository.findByDocumentType(documentType));
        }

        return ResponseEntity.ok(documentTypeConfigurations.stream()
                .map(x -> documentTypeConfigurationMapper.toDocumentTypeConfiguration(x))
        .collect(Collectors.toList()));

    }

    @Override
    public ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration> createDocumentTypeConfiguration(DocumentTypeConfigurationRequest documentTypeConfigurationRequest) {

        if(documentTypeConfigurationRepository.findByDocumentType(documentTypeConfigurationRequest.getDocumentType().getType()) != null) {
            throw new AiReviewerDocumentTypeConfigurationException("There is already a document of that type");
        }

        DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                .builder()
                .documentType(documentTypeConfigurationRequest.getDocumentType().getType())
                .documentTypeDescription(documentTypeConfigurationRequest.getDocumentType().getDescription())
                .projectId(documentTypeConfigurationRequest.getProjectId())
                .documentConfig((LinkedHashMap<String, Object>) documentTypeConfigurationRequest.getDocumentConfig())
                .create();

        DocumentTypeConfiguration savedDocument = documentTypeConfigurationRepository.save(documentTypeConfiguration);

        return ResponseEntity.ok(documentTypeConfigurationMapper.toDocumentTypeConfiguration(savedDocument));

    }

    @Override
    public ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration> updateDocumentType(ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration documentTypeConfiguration) {

        if (!documentTypeConfigurationRepository.existsByDocumentTypeAndId(documentTypeConfiguration.getDocumentType().getType(), documentTypeConfiguration.getId())) throw new AiReviewerDocumentTypeConfigurationException("No matches found for that type and id");
        if (!documentTypeConfiguration.getDocumentType().getType().equals(documentTypeConfigurationRepository.findById(documentTypeConfiguration.getId()).get().getDocumentType())) throw new AiReviewerDocumentTypeConfigurationException("Document type cannot be updated");

        DocumentTypeConfiguration updateDocumentTypeConfiguration = DocumentTypeConfiguration
                .builder()
                .documentType(documentTypeConfiguration.getDocumentType().getType())
                .documentTypeDescription(documentTypeConfiguration.getDocumentType().getDescription())
                .projectId(documentTypeConfiguration.getProjectId())
                .documentConfig((LinkedHashMap<String, Object>) documentTypeConfiguration.getDocumentConfig())
                .create();

        updateDocumentTypeConfiguration.setId(documentTypeConfiguration.getId());

        DocumentTypeConfiguration updatedDocumentTypeConfiguration = documentTypeConfigurationRepository.save(updateDocumentTypeConfiguration);

        return ResponseEntity.ok(documentTypeConfigurationMapper.toDocumentTypeConfiguration(updatedDocumentTypeConfiguration));

    }



}
