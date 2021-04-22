package ca.bc.gov.open.jag.efilingreviewerapi.documentconfiguration;

import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteDocumentConfigurationsTest {

    private static final UUID DELETE_ID = UUID.randomUUID();
    private static final UUID NOT_FOUND_ID = UUID.randomUUID();

    private DocumentConfigurationsApiDelegateImpl sut;
    @Mock
    private DocumentTypeConfigurationRepository documentTypeConfigurationRepositoryMock;

    @BeforeAll
    public void beforeAll() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(documentTypeConfigurationRepositoryMock.existsById(ArgumentMatchers.eq(DELETE_ID))).thenReturn(true);

        Mockito.when(documentTypeConfigurationRepositoryMock.existsById(ArgumentMatchers.eq(NOT_FOUND_ID))).thenReturn(false);

        Mockito.doNothing().when(documentTypeConfigurationRepositoryMock).deleteById(ArgumentMatchers.eq(DELETE_ID));

        sut = new DocumentConfigurationsApiDelegateImpl(documentTypeConfigurationRepositoryMock, null);
    }

    @Test
    @DisplayName("no content: DELETE /documentTypeConfigurations the configuration was deleted")
    public void withValidRequestDeleteDocumentType() {

        ResponseEntity actual = sut.deleteDocumentTypeConfiguration(DELETE_ID);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    @Test
    @DisplayName("not found: DELETE /documentTypeConfigurations the configuration was not found")
    public void withValidRequestDocumentTypeNotFound() {

        ResponseEntity actual = sut.deleteDocumentTypeConfiguration(NOT_FOUND_ID);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

}
