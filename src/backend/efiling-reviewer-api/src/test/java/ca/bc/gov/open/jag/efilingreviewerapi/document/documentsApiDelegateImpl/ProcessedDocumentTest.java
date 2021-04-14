package ca.bc.gov.open.jag.efilingreviewerapi.document.documentsApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.ProcessedDocument;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.DocumentTypeConfigurationRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ProcessedDocumentMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mocks.ExtractRequestMockFactory;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessedDocumentTest {

    private static final String APPLICATION_PDF = "application/pdf";
    private static final String CASE_1 = "test-document.pdf";
    private static final String CASE_2 = "test.txt";
    public static final String DOCUMENT_TYPE = "RCC";
    public static final String NOT_RCC = "NOT_RCC";

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
                .when(extractStoreMock.put(Mockito.eq(BigDecimal.ONE), Mockito.any(ExtractRequest.class)))
                .thenReturn(Optional.of(ExtractRequestMockFactory.mock()));

        Mockito
                .when(extractStoreMock.put(Mockito.eq(BigDecimal.TEN), Mockito.any(ExtractRequest.class)))
                .thenReturn(Optional.empty());

        Mockito.doNothing().when(stringRedisTemplateMock).convertAndSend(any(), any());

        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl(new ExtractMapperImpl());
        sut = new DocumentsApiDelegateImpl(diligenServiceMock, extractRequestMapper, extractStoreMock, stringRedisTemplateMock, fieldProcessorMock, documentValidatorMock, documentTypeConfigurationRepositoryMock, new ProcessedDocumentMapperImpl());

    }

    //@Test
    @DisplayName("200: Assert processed document is returned")
    public void withUserHavingValidRequestShouldReturnCreated() throws IOException, VirusDetectedException {

        Mockito.when(diligenServiceMock.postDocument(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ONE);

        Path path = Paths.get("src/test/resources/" + CASE_1);

        UUID transactionId = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        Mockito.doNothing().when(documentValidatorMock).validateDocument(any(), any());

        ResponseEntity<ProcessedDocument> actual = sut.documentProcessed(UUID.randomUUID(), BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    //@Test
    @DisplayName("Error: ")
    public void withNoCacheThrowException() throws IOException {

        Mockito.when(diligenServiceMock.postDocument(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.doThrow(AiReviewerDocumentException.class).when(documentValidatorMock).validateDocument(any(), any());

        Assertions.assertThrows(AiReviewerCacheException.class,() ->sut.documentProcessed(UUID.randomUUID(), BigDecimal.ONE));

    }

}
