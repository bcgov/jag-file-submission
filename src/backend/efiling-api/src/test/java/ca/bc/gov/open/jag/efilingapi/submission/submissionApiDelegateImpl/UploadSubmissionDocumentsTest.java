package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.UploadSubmissionDocumentsResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.DOCUMENT_REQUIRED;
import static ca.bc.gov.open.jag.efilingapi.error.ErrorResponse.DOCUMENT_STORAGE_FAILURE;

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

    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.when(resourceMock.getFilename()).thenReturn("file.txt");
        Mockito.when(multipartFileMock.getResource()).thenReturn(resourceMock);
        Mockito.when(multipartFileMock.getBytes()).thenThrow(new IOException("random"));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock);
    }

    @Test
    @DisplayName("200: with files should return ok")
    public void withFilesShouldReturnOk() {

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test".getBytes());
        files.add(multipartFile);
        files.add(multipartFile);
        ResponseEntity<UploadSubmissionDocumentsResponse> actual = sut.uploadSubmissionDocuments(UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertNotNull(actual.getBody().getSubmissionId());
        Assertions.assertEquals(new BigDecimal(2), actual.getBody().getReceived());
    }

    @Test
    @DisplayName("400: with empty files should return bad request")
    public void withEmptyFilesShouldReturnBadRequest() {

        List<MultipartFile> files = new ArrayList<>();
        ResponseEntity actual = sut.uploadSubmissionDocuments(UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }


    @Test
    @DisplayName("400: with null files should return bad request")
    public void withNullFilesShouldReturnBadRequest() {

        ResponseEntity actual = sut.uploadSubmissionDocuments(UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_REQUIRED.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }

    @Test
    @DisplayName("500: with ioException should return 500")
    public void withIoExceptionShouldReturnInternalServer() {

        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFileMock);
        ResponseEntity actual = sut.uploadSubmissionDocuments(UUID.randomUUID(), files);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(DOCUMENT_STORAGE_FAILURE.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(DOCUMENT_STORAGE_FAILURE.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }

}
