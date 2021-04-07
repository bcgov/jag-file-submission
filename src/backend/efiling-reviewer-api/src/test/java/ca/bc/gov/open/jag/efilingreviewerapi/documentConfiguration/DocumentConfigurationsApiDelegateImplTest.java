package ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration.mappers.DocumentTypeConfigurationMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration.mappers.DocumentTypeConfigurationMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentConfigurationsApiDelegateImplTest {

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
                .documentType("TEST")
                .create();

        documentTypeConfigurationList.add(documentTypeConfiguration);

        Mockito.when(documentTypeConfigurationRepositoryMock.findAll()).thenReturn(documentTypeConfigurationList);

        DocumentTypeConfigurationMapper documentTypeConfigurationMapper = new DocumentTypeConfigurationMapperImpl();
        sut = new DocumentConfigurationsApiDelegateImpl(documentTypeConfigurationRepositoryMock, documentTypeConfigurationMapper);

    }


    @Test
    @DisplayName("ok: GET /documentTypeConfigurations Should Return a list of document")
    public void shouldReturnAListOfDocumentConfiguration() {

        ResponseEntity<List<ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration>> response = sut.getDocumentConfigurations();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().stream().count());
        Assertions.assertEquals("TEST", response.getBody().get(0).getDocumentType());


    }


}
