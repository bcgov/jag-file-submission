package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration.mappers.DocumentTypeConfigurationMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetDocumentConfigurationsTest {
    private static final String CASE_1 = "CASE1";
    private static final String CASE_2 = "CASE2";
    private static final String DESCRIPTION = "DESCRIPTION";

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

        Mockito.when(documentTypeConfigurationRepositoryMock.findAll()).thenReturn(documentTypeConfigurationList);

        Mockito.when(documentTypeConfigurationRepositoryMock.findByDocumentType(CASE_1)).thenReturn(documentTypeConfiguration);

        DocumentTypeConfiguration documentTypeConfiguration2 = DocumentTypeConfiguration
                .builder()
                .documentConfig(new DocumentConfig())
                .documentType("CASE2")
                .create();

        Mockito.doReturn(documentTypeConfiguration2).when(documentTypeConfigurationRepositoryMock).save(ArgumentMatchers.argThat(x -> x.getDocumentType().equals(CASE_2)));

        DocumentTypeConfigurationMapper documentTypeConfigurationMapper = new DocumentTypeConfigurationMapperImpl();
        sut = new DocumentConfigurationsApiDelegateImpl(documentTypeConfigurationRepositoryMock, documentTypeConfigurationMapper);

    }


    @Test
    @DisplayName("ok: GET /documentTypeConfigurations Should Return a list of document")
    public void shouldReturnAListOfDocumentConfiguration() {

        ResponseEntity<List<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration>> response = sut.getDocumentConfigurations("");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().stream().count());
        Assertions.assertEquals(CASE_1, response.getBody().get(0).getDocumentType().getType());

    }


    @Test
    @DisplayName("ok: GET /documentTypeConfigurations Should Return a single  document")
    public void shouldReturnASingleOfDocumentConfiguration() {

        ResponseEntity<List<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration>> response = sut.getDocumentConfigurations(CASE_1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().stream().count());
        Assertions.assertEquals(CASE_1, response.getBody().get(0).getDocumentType().getType());

    }

}
