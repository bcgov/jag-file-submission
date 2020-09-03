package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.UploadSubmissionDocumentsResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
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
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.*;
import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.FILE_TYPE_ERROR;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Upload Additional Submission Documents Test Suite")
public class uploadAdditionalSubmissionDocumentsTest {
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

    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.when(resourceMock.getFilename()).thenReturn("file.txt");
        Mockito.when(multipartFileMock.getResource()).thenReturn(resourceMock);
        Mockito.when(multipartFileMock.getBytes()).thenThrow(new IOException("random"));

        Submission submission = Submission
                .builder()
                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper);
    }

    @Test
    @DisplayName("200: with pdf files should return ok")
    public void withPdfFilesShouldReturnOk() throws IOException, VirusDetectedException {

        File file = new File("src/test/resources/test.pdf");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));

        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        ResponseEntity<UploadSubmissionDocumentsResponse> actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(TestHelpers.CASE_1, actual.getBody().getSubmissionId());
        Assertions.assertEquals(new BigDecimal(2), actual.getBody().getReceived());
    }

    @Test
    @DisplayName("400: with non pdf files should return bad request")
    public void withNonPdfFilesShouldReturnBadRequest() throws IOException, VirusDetectedException {

        File file = new File("src/test/resources/test.txt");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(FILE_TYPE_ERROR.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(FILE_TYPE_ERROR.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: with empty files should return bad request")
    public void withEmptyFilesShouldReturnBadRequest() {

        List<MultipartFile> files = new ArrayList<>();
        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }


    @Test
    @DisplayName("400: with null files should return bad request")
    public void withNullFilesShouldReturnBadRequest() {

        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }

    @Test
    @DisplayName("404: with no submission present should return not found")
    public void withNoSubmissionReturnNotFound() {

        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_2, UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("500: with ioException should return 500")
    public void withIoExceptionShouldReturnInternalServerError() {

        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFileMock);
        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_STORAGE_FAILURE.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_STORAGE_FAILURE.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
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

        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_STORAGE_FAILURE.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_STORAGE_FAILURE.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }
}
