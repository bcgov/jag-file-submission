package ca.bc.gov.open.jag.efilingreviewerapi.document.documentsApiDelegateImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ProcessedDocumentMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mocks.ExtractResponseMockFactory;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessedDocumentTest {

    private DocumentsApiDelegateImpl sut;

    @Mock
    private DiligenService diligenServiceMock;

    @Mock
    private DocumentValidator documentValidatorMock;

    @Mock
    private ExtractStore extractStoreMock;

    @Mock
    private StringRedisTemplate stringRedisTemplateMock;

    @Mock
    private FieldProcessor fieldProcessorMock;

    @Mock
    private DocumentTypeConfigurationRepository documentTypeConfigurationRepositoryMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito
                .when(extractStoreMock.getResponse(Mockito.eq(BigDecimal.ONE)))
                .thenReturn(Optional.of(ExtractResponseMockFactory.mock()));

        Mockito
                .when(extractStoreMock.getResponse(Mockito.eq(BigDecimal.TEN)))
                .thenReturn(Optional.empty());

        Mockito.doNothing().when(extractStoreMock).evict(Mockito.any());

        Mockito.doNothing().when(extractStoreMock).evictResponse(Mockito.any());

        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl(new ExtractMapperImpl());
        sut = new DocumentsApiDelegateImpl(diligenServiceMock, extractRequestMapper, extractStoreMock, stringRedisTemplateMock, fieldProcessorMock, documentValidatorMock, documentTypeConfigurationRepositoryMock, new ProcessedDocumentMapperImpl(), null);

    }

    @Test
    @DisplayName("200: Assert processed document is returned")
    public void withUserHavingValidRequestShouldReturnCreated() {

        ResponseEntity<ProcessedDocument> actual = sut.documentProcessed(UUID.randomUUID(), BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    @DisplayName("Error: no document found ")
    public void withNoCacheThrowException() {

        Assertions.assertThrows(AiReviewerCacheException.class,() ->sut.documentProcessed(UUID.randomUUID(), BigDecimal.TEN));

    }

}
