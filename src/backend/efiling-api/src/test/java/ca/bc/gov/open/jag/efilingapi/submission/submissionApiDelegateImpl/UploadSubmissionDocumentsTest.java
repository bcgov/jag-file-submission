package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.UploadSubmissionDocumentsResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.*;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Upload Submission Documents Test Suite")
public class UploadSubmissionDocumentsTest {

    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private NavigationProperties navigationProperties;

    @Mock
    private MultipartFile multipartFileMock;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private Resource resourceMock;

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private ClamAvService clamAvServiceMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;


    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        Mockito.when(resourceMock.getFilename()).thenReturn("file.pdf");
        Mockito.when(multipartFileMock.getResource()).thenReturn(resourceMock);
        Mockito.when(multipartFileMock.getBytes()).thenThrow(new IOException("random"));

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
    }

    @Test
    @DisplayName("200: with files should return ok")
    public void withFilesShouldReturnOk() throws IOException, VirusDetectedException {

        File file = new File("src/test/resources/test.pdf");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        ResponseEntity<UploadSubmissionDocumentsResponse> actual = sut.uploadSubmissionDocuments(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), files);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertNotNull(actual.getBody().getSubmissionId());
        Assertions.assertEquals(new BigDecimal(2), actual.getBody().getReceived());
    }

    @Test
    @DisplayName("400: with non pdf files should throw FileTypeException")
    public void withNonPdfFilesShouldThrowFileTypeException() throws IOException, VirusDetectedException {

        File file = new File("src/test/resources/test.txt");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        FileTypeException exception = Assertions.assertThrows(FileTypeException.class, () -> sut.uploadSubmissionDocuments(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), files));
        Assertions.assertEquals(ErrorCode.FILE_TYPE_ERROR.toString(), exception.getErrorCode());
    }


    @Test
    @DisplayName("400: with empty files should throw DocumentRequiredException")
    public void withEmptyFilesShouldThrowDocumentRequiredException() {

        List<MultipartFile> files = new ArrayList<>();
        DocumentRequiredException exception = Assertions.assertThrows(DocumentRequiredException.class, () -> sut.uploadSubmissionDocuments(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), files));
        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(), exception.getErrorCode());
    }


    @Test
    @DisplayName("400: with null files should throw DocumentRequiredException")
    public void withNullFilesShouldThrowDocumentRequiredException() {

        DocumentRequiredException exception = Assertions.assertThrows(DocumentRequiredException.class, () -> sut.uploadSubmissionDocuments(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), null));
        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("500: with ioException should return 500")
    public void withIoExceptionShouldReturnInternalServerError() {

        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFileMock);

        DocumentStorageException exception = Assertions.assertThrows(DocumentStorageException.class, () -> sut.uploadSubmissionDocuments(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), files));
        Assertions.assertEquals(ErrorCode.DOCUMENT_STORAGE_FAILURE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("502: with ioException should return 502")
    public void withScanFailureShouldReturnBadGateway() throws VirusDetectedException, IOException {


        File file = new File("src/test/resources/test.pdf");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doThrow(VirusDetectedException.class).when(clamAvServiceMock).scan(any());

        DocumentStorageException exception = Assertions.assertThrows(DocumentStorageException.class, () -> sut.uploadSubmissionDocuments(UUID.randomUUID(), UUID.randomUUID().toString().replace("-", ""), files));
        Assertions.assertEquals(ErrorCode.DOCUMENT_STORAGE_FAILURE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: with missing id should throw InvalidUniversalException")
    public void withBlankIdShouldThrowInvalidUniversalException() throws VirusDetectedException, IOException {

        File file = new File("src/test/resources/test.pdf");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doThrow(VirusDetectedException.class).when(clamAvServiceMock).scan(any());

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.uploadSubmissionDocuments(UUID.randomUUID(), "  ", files));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }
}
