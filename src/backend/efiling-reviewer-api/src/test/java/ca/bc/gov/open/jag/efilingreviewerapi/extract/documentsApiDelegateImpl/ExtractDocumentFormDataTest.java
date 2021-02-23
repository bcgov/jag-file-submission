package ca.bc.gov.open.jag.efilingreviewerapi.extract.documentsApiDelegateImpl;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.DocumentsApiDelegateImpl;
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


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentsApiDelegateImpl test suite")
public class ExtractDocumentFormDataTest {

    private static final String APPLICATION_PDF = "application/pdf";
    private static final String CASE_1 = "test-document.pdf";
    private DocumentsApiDelegateImpl sut;

    @Mock
    DiligenService diligenService;

    @Mock
    private ExtractStore extractStore;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(extractStore.put(Mockito.any(), Mockito.any())).thenReturn(Optional.of(ExtractRequestMockFactory.mock()));

        ExtractRequestMapper extractRequestMapper = new ExtractRequestMapperImpl();
        sut = new DocumentsApiDelegateImpl(diligenService, extractRequestMapper, extractStore);

    }
    @Test
    @DisplayName("200: Assert something returned")
    public void withUserHavingValidRequestShouldReturnCreated() throws IOException {

        Mockito.when(diligenService.postDocument(Mockito.any(), Mockito.any())).thenReturn(BigDecimal.ONE);

        Path path = Paths.get("src/test/resources/" + CASE_1);

        UUID transactionId = UUID.randomUUID();

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        ResponseEntity<DocumentExtractResponse> actual = sut.extractDocumentFormData(transactionId, "TYPE", multipartFile);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_CONTENT_TYPE, actual.getBody().getDocument().getContentType());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_FILE_NAME, actual.getBody().getDocument().getFileName());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_SIZE, actual.getBody().getDocument().getSize());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_EXTRACT_ID, actual.getBody().getExtract().getId());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_EXTRACT_TRANSACTION_ID, actual.getBody().getExtract().getTransactionId());

    }
}
