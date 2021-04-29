package ca.bc.gov.open.jag.efilingreviewerapi.document.documentsApiDelegateImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerInvalidTransactionIdException;
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

    private static final UUID EXPECTED_EXTRACT_TRANSACTION_ID = UUID.fromString("e40fb65d-1b61-40b9-9deb-d8eb3bab877f");
    private static final UUID EXPECTED_EXTRACT_ID = UUID.fromString("89714a2a-611e-4f36-811b-1b2eb4cce7ee");
    private static final String EXPECTED_DOCUMENT_CONTENT_TYPE = "application/pdf";
    private static final String EXPECTED_DOCUMENT_FILE_NAME = "myfile.pdf";
    private static final String EXPECTED_DOCUMENT_TYPE = "type";
    private static final BigDecimal EXPECTED_DOCUMENT_SIZE = BigDecimal.TEN;

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

        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl();

        Mockito.doNothing().when(extractStoreMock).evict(Mockito.any());

        Mockito.doNothing().when(extractStoreMock).evictResponse(Mockito.any());

        sut = new DocumentsApiDelegateImpl(diligenServiceMock, extractRequestMapper, extractStoreMock, stringRedisTemplateMock, fieldProcessorMock, documentValidatorMock, documentTypeConfigurationRepositoryMock, new ProcessedDocumentMapperImpl(), null);

    }

    @Test
    @DisplayName("200: Assert processed document is returned")
    public void withUserHavingValidRequestShouldReturnCreated() {

        ResponseEntity<ProcessedDocument> actual = sut.documentProcessed(EXPECTED_EXTRACT_TRANSACTION_ID, BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(EXPECTED_EXTRACT_TRANSACTION_ID, actual.getBody().getExtract().getTransactionId());
        Assertions.assertEquals(EXPECTED_EXTRACT_ID, actual.getBody().getExtract().getId());

        Assertions.assertEquals(EXPECTED_DOCUMENT_CONTENT_TYPE, actual.getBody().getDocument().getContentType());
        Assertions.assertEquals(EXPECTED_DOCUMENT_FILE_NAME, actual.getBody().getDocument().getFileName());
        Assertions.assertEquals(EXPECTED_DOCUMENT_TYPE, actual.getBody().getDocument().getType());
        Assertions.assertEquals(EXPECTED_DOCUMENT_SIZE, actual.getBody().getDocument().getSize());

    }

    @Test
    @DisplayName("Error: no document found ")
    public void withNoCacheThrowException() {

        Assertions.assertThrows(AiReviewerCacheException.class,() -> sut.documentProcessed(UUID.randomUUID(), BigDecimal.TEN));

    }

    @Test
    @DisplayName("Error: no mismatched transaction id ")
    public void withInvalidTransactionIdThrowsException() {

        Assertions.assertThrows(AiReviewerInvalidTransactionIdException.class,() -> sut.documentProcessed(UUID.randomUUID(), BigDecimal.ONE));

    }

}
