package ca.bc.gov.open.jag.efilingreviewerapi.document.validators.documentValidator;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidator;
import ca.bc.gov.open.jag.efilingreviewerapi.document.validators.DocumentValidatorImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerVirusFoundException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentsValidatorImpl test suite")
public class ValidateDocumentTest {
    DocumentValidator sut;

    private static final String CASE_1 = "test-document.pdf";
    private static final String CASE_2 = "test.txt";
    private static final String DOCUMENT_TYPE = "RCC";
    private static final String NOT_RCC = "NOT_RCC";
    private static final String APPLICATION_PDF = "application/pdf";
    private static final String TEXT_PLAIN = "text/plain";

    @Mock
    ClamAvService clamAvServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new DocumentValidatorImpl(clamAvServiceMock);

    }

    @Test
    @DisplayName("Ok: Assert no error")
    public void whenValidRequestNoExceptionThrown() throws VirusDetectedException, IOException {

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        Path path = Paths.get("src/test/resources/" + CASE_1);

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));


        Assertions.assertDoesNotThrow(() -> sut.validateDocument(DOCUMENT_TYPE, multipartFile));

    }

    @Test
    @DisplayName("Error: Assert document error")
    public void whenInvalidDocumentTypeExceptionThrown() throws IOException, VirusDetectedException {

        Assertions.assertThrows(AiReviewerDocumentException.class, () -> sut.validateDocument(NOT_RCC, null));

    }

    @Test
    @DisplayName("Error: Assert document error")
    public void whenNotAPDFExceptionThrown() throws VirusDetectedException, IOException {

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        Path path = Paths.get("src/test/resources/" + CASE_2);

        MultipartFile multipartFile = new MockMultipartFile(CASE_2,
                CASE_2, TEXT_PLAIN, Files.readAllBytes(path));

        Assertions.assertThrows(AiReviewerDocumentException.class, () -> sut.validateDocument(DOCUMENT_TYPE, multipartFile));

    }

    @Test
    @DisplayName("Error: Assert virus error")
    public void whenVirsuFoundExceptionThrown() throws VirusDetectedException, IOException {

        Mockito.doThrow(VirusDetectedException.class).when(clamAvServiceMock).scan(any());

        Path path = Paths.get("src/test/resources/" + CASE_1);

        MultipartFile multipartFile = new MockMultipartFile(CASE_1,
                CASE_1, APPLICATION_PDF, Files.readAllBytes(path));

        Assertions.assertThrows(AiReviewerVirusFoundException.class, () -> sut.validateDocument(DOCUMENT_TYPE, multipartFile));

    }

    @Test
    @DisplayName("Error: Assert document error")
    public void whenFileCorruptExceptionThrown() throws VirusDetectedException, IOException {
        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        MockMultipartFile mockMultipartFileException = Mockito.mock(MockMultipartFile.class);
        Mockito.when(mockMultipartFileException.getBytes()).thenThrow(IOException.class);

        Assertions.assertThrows(AiReviewerDocumentException.class, () -> sut.validateDocument(DOCUMENT_TYPE, mockMultipartFileException));

    }

}
