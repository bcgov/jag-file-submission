package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentTypeConfigurationException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateDocumentConfigurationsTest {
    private static final String CASE_1 = "CASE1";
    private static final String CASE_2 = "CASE2";
    private static final String CASE_3 = "CASE3";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final UUID CASE_1_ID = UUID.randomUUID();
    private static final UUID CASE_2_ID = UUID.randomUUID();
    private static final UUID CASE_3_ID = UUID.randomUUID();

    private DocumentConfigurationsApiDelegateImpl sut;
    @Mock
    private DocumentTypeConfigurationRepository documentTypeConfigurationRepositoryMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        List<DocumentTypeConfiguration> documentTypeConfigurationList = new ArrayList<>();

        DocumentTypeConfiguration documentTypeConfiguration = DocumentTypeConfiguration
                .builder()
                .documentConfig(new DocumentConfig())
                .documentType(CASE_1)
                .create();

        documentTypeConfigurationList.add(documentTypeConfiguration);

        Mockito.when(documentTypeConfigurationRepositoryMock.findById(ArgumentMatchers.eq(CASE_1_ID))).thenReturn(Optional.of(documentTypeConfiguration));

        Mockito.when(documentTypeConfigurationRepositoryMock.findById(ArgumentMatchers.eq(CASE_2_ID))).thenReturn(Optional.of(documentTypeConfiguration));

        Mockito.when(documentTypeConfigurationRepositoryMock.findByDocumentType(CASE_1)).thenReturn(documentTypeConfiguration);

        Mockito.when(documentTypeConfigurationRepositoryMock.existsByDocumentTypeAndId(ArgumentMatchers.eq(CASE_3), ArgumentMatchers.eq(CASE_3_ID))).thenReturn(false);

        Mockito.when(documentTypeConfigurationRepositoryMock.existsByDocumentTypeAndId(ArgumentMatchers.eq(CASE_1), ArgumentMatchers.eq(CASE_1_ID))).thenReturn(true);

        Mockito.when(documentTypeConfigurationRepositoryMock.existsByDocumentTypeAndId(ArgumentMatchers.eq(CASE_2), ArgumentMatchers.eq(CASE_2_ID))).thenReturn(true);

        DocumentTypeConfiguration documentTypeConfiguration2 = DocumentTypeConfiguration
                .builder()
                .documentConfig(new DocumentConfig())
                .documentType("CASE1")
                .create();

        Mockito.doReturn(documentTypeConfiguration2).when(documentTypeConfigurationRepositoryMock).save(any());

        DocumentTypeConfigurationMapper documentTypeConfigurationMapper = new DocumentTypeConfigurationMapperImpl();
        sut = new DocumentConfigurationsApiDelegateImpl(documentTypeConfigurationRepositoryMock, documentTypeConfigurationMapper);

    }

    @Test
    @DisplayName("ok: PUT /documentTypeConfigurations should update a document type configuration")
    public void withValidDocumentShouldUpdateConfiguration() {

        ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration documentTypeConfigurationRequest = new ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration();
        DocumentType documentType = new DocumentType();
        documentType.setType(CASE_1);
        documentType.setDescription(DESCRIPTION);
        documentTypeConfigurationRequest.setDocumentType(documentType);
        documentTypeConfigurationRequest.setProjectId(1);
        documentTypeConfigurationRequest.setId(CASE_1_ID);

        LinkedHashMap<String, Object> testProperty = new LinkedHashMap<>();

        testProperty.put("type", "string");
        testProperty.put("fieldId", "230");

        LinkedHashMap<String, Object> courtProperties = new LinkedHashMap<>();
        courtProperties.put("test", testProperty);

        LinkedHashMap<String, Object> courtProperty = new LinkedHashMap<>();
        courtProperty.put("type", "object");
        courtProperty.put("properties", courtProperties);

        LinkedHashMap<String, Object> documentProperties = new LinkedHashMap<>();
        documentProperties.put("court", courtProperty);

        LinkedHashMap<String, Object> documentConfig = new LinkedHashMap<>();
        documentConfig.put("properties", documentProperties);

        documentTypeConfigurationRequest.setDocumentConfig(documentConfig);

        ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration> response = sut.updateDocumentTypeConfiguration(documentTypeConfigurationRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals("CASE1", response.getBody().getDocumentType().getType());

    }

    @Test
    @DisplayName("Error: PUT /documentTypeConfigurations should return bad request if the document type doesn't exists")
    public void withNonExistingDocumentShouldReturnError() {

        ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration documentTypeConfigurationRequest = new ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration();
        DocumentType documentType = new DocumentType();
        documentType.setType(CASE_2);
        documentType.setDescription(DESCRIPTION);
        documentTypeConfigurationRequest.setDocumentType(documentType);
        documentTypeConfigurationRequest.setProjectId(1);
        documentTypeConfigurationRequest.setId(CASE_2_ID);

        LinkedHashMap<String, Object> testProperty = new LinkedHashMap<>();

        testProperty.put("type", "string");
        testProperty.put("fieldId", "230");

        LinkedHashMap<String, Object> courtProperties = new LinkedHashMap<>();
        courtProperties.put("test", testProperty);

        LinkedHashMap<String, Object> courtProperty = new LinkedHashMap<>();
        courtProperty.put("type", "object");
        courtProperty.put("properties", courtProperties);

        LinkedHashMap<String, Object> documentProperties = new LinkedHashMap<>();
        documentProperties.put("court", courtProperty);

        LinkedHashMap<String, Object> documentConfig = new LinkedHashMap<>();
        documentConfig.put("properties", documentProperties);

        documentTypeConfigurationRequest.setDocumentConfig(documentConfig);

        Assertions.assertThrows(AiReviewerDocumentTypeConfigurationException.class, () -> sut.updateDocumentTypeConfiguration(documentTypeConfigurationRequest));

    }

    @Test
    @DisplayName("Error: PUT /documentTypeConfigurations should return bad request if the document type doesn't exists")
    public void withMismatchingDocumentTypeDocumentShouldReturnError() {

        ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration documentTypeConfigurationRequest = new ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration();
        DocumentType documentType = new DocumentType();
        documentType.setType(CASE_3);
        documentType.setDescription(DESCRIPTION);
        documentTypeConfigurationRequest.setDocumentType(documentType);
        documentTypeConfigurationRequest.setProjectId(1);
        documentTypeConfigurationRequest.setId(CASE_3_ID);

        LinkedHashMap<String, Object> testProperty = new LinkedHashMap<>();

        testProperty.put("type", "string");
        testProperty.put("fieldId", "230");

        LinkedHashMap<String, Object> courtProperties = new LinkedHashMap<>();
        courtProperties.put("test", testProperty);

        LinkedHashMap<String, Object> courtProperty = new LinkedHashMap<>();
        courtProperty.put("type", "object");
        courtProperty.put("properties", courtProperties);

        LinkedHashMap<String, Object> documentProperties = new LinkedHashMap<>();
        documentProperties.put("court", courtProperty);

        LinkedHashMap<String, Object> documentConfig = new LinkedHashMap<>();
        documentConfig.put("properties", documentProperties);

        documentTypeConfigurationRequest.setDocumentConfig(documentConfig);


        Assertions.assertThrows(AiReviewerDocumentTypeConfigurationException.class, () -> sut.updateDocumentTypeConfiguration(documentTypeConfigurationRequest));

    }

}
