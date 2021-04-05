package ca.bc.gov.open.jag.efilingreviewerapi.document;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeedDocumentTypeConfigurationTest {

    private SeedDocumentTypeConfiguration sut;

    @Mock
    private DocumentTypeConfigurationRepository documentTypeConfigurationRepositoryMock;

    @Mock
    private ContextRefreshedEvent contextRefreshEventMock;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new SeedDocumentTypeConfiguration(documentTypeConfigurationRepositoryMock);

    }

    @Test
    @DisplayName("ok: with no config should seed data")
    public void withNoConfigShouldSeedData() {

        Mockito.when(documentTypeConfigurationRepositoryMock.findByDocumentType("RCC")).thenReturn(null);
        sut.seed(contextRefreshEventMock);
        Mockito.verify(documentTypeConfigurationRepositoryMock, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    @DisplayName("ok: with existing config should not seed data")
    public void withConfigShouldNotSeedData() {

        DocumentTypeConfiguration config = DocumentTypeConfiguration.builder().create();
        Mockito.when(documentTypeConfigurationRepositoryMock.findByDocumentType("RCC")).thenReturn(config);
        sut.seed(contextRefreshEventMock);
        Mockito.verify(documentTypeConfigurationRepositoryMock, Mockito.times(0)).save(Mockito.any());

    }


}
