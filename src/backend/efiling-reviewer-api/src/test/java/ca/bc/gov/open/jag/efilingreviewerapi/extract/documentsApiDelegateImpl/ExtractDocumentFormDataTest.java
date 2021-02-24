package ca.bc.gov.open.jag.efilingreviewerapi.extract.documentsApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerVirusFoundException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerCacheException;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers.ExtractRequestMapperImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.mocks.ExtractRequestMockFactory;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.store.ExtractStore;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
@DisplayName("DocumentsApiDelegateImpl test suite")
public class ExtractDocumentFormDataTest {

    private static final String APPLICATION_PDF = "application/pdf";
    private static final String CASE_1 = "test-document.pdf";
    private static final String CASE_2 = "test.txt";
    private DocumentsApiDelegateImpl sut;

    @Mock
    DiligenService diligenService;

    @Mock
    ClamAvService clamAvService;

    @Mock
    private ExtractStore extractStore;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(extractStore.put(Mockito.eq(BigDecimal.ONE), Mockito.any())).thenReturn(Optional.of(ExtractRequestMockFactory.mock()));
        Mockito.when(extractStore.put(Mockito.eq(BigDecimal.TEN), Mockito.any())).thenReturn(Optional.empty());

        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl(new ExtractMapperImpl());
        sut = new DocumentsApiDelegateImpl(diligenService, extractRequestMapper, extractStore, clamAvService);

    }
    @Test
    @DisplayName("200: Assert something returned")
    public void withUserHavingValidRequestShouldReturnCreated() throws IOException, VirusDetectedException {

        Mockito.when(diligenService.postDocument(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ONE);

        Path path = Paths.get("src/test/resources/" + CASE_1);

        UUID transactionId = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        Mockito.doNothing().when(clamAvService).scan(any());

        ResponseEntity<DocumentExtractResponse> actual = sut.extractDocumentFormData(transactionId, "TYPE", multipartFile);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_CONTENT_TYPE, actual.getBody().getDocument().getContentType());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_FILE_NAME, actual.getBody().getDocument().getFileName());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_SIZE, actual.getBody().getDocument().getSize());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_EXTRACT_ID, actual.getBody().getExtract().getId());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_EXTRACT_TRANSACTION_ID, actual.getBody().getExtract().getTransactionId());

    }


    @Test
    @DisplayName("200: Assert something returned")
    public void withUserHavingValidRequestShouldReturnCredfated() throws IOException {

        Mockito.when(diligenService.postDocument(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.TEN);

        Path path = Paths.get("src/test/resources/" + CASE_1);

        UUID transactionId = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        Assertions.assertThrows(AiReviewerCacheException.class,
                () -> sut.extractDocumentFormData(transactionId, "TYPE", multipartFile)
        );

    }

    @Test
    @DisplayName("Error: non pdf throws exception")
    public void withNonPdfFilesShouldReturnOk() throws IOException, VirusDetectedException {

        Mockito.when(diligenService.postDocument(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.doNothing().when(clamAvService).scan(any());

        Path path = Paths.get("src/test/resources/" + CASE_2);

        UUID transactionId = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        Assertions.assertThrows(AiReviewerDocumentException.class,() -> sut.extractDocumentFormData(transactionId, "TYPE", multipartFile));

    }

    @Test
    @DisplayName("Error: IO exception")
    public void withScanFailureThrowException() throws VirusDetectedException, IOException {

        Mockito.doNothing().when(clamAvService).scan(any());
        Path path = Paths.get("src/test/resources/" + CASE_1);

        UUID transactionId = UUID.randomUUID();

        MockMultipartFile mockMultipartFileException = Mockito.mock(MockMultipartFile.class);
        Mockito.when(mockMultipartFileException.getBytes()).thenThrow(IOException.class);

        Assertions.assertThrows(AiReviewerDocumentException.class,() -> sut.extractDocumentFormData(transactionId, "TYPE", mockMultipartFileException));

    }


    @Test
    @DisplayName("Error: fails virus scan")
    public void withScanFailureShouldReturnBadGateway() throws VirusDetectedException, IOException {

        Mockito.doThrow(VirusDetectedException.class).when(clamAvService).scan(any());
        Path path = Paths.get("src/test/resources/" + CASE_1);

        UUID transactionId = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        Assertions.assertThrows(AiReviewerVirusFoundException.class,() -> sut.extractDocumentFormData(transactionId, "TYPE", multipartFile));

    }

}
