package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.UploadSubmissionDocumentsResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.*;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Upload Additional Submission Documents Test Suite")
public class UploadAdditionalSubmissionDocumentsTest {

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
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.when(resourceMock.getFilename()).thenReturn("file.txt");
        Mockito.when(multipartFileMock.getResource()).thenReturn(resourceMock);
        Mockito.when(multipartFileMock.getBytes()).thenThrow(new IOException("random"));

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        Submission submission = Submission
                .builder()
                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
    }

    @Test
    @DisplayName("200: with pdf files should return ok")
    public void withPdfFilesShouldReturnOk() throws IOException, VirusDetectedException {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

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
    @DisplayName("400: with non pdf files should throw FileTypeException")
    public void withNonPdfFilesShouldThrowFileTypeException() throws IOException, VirusDetectedException {


        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        File file = new File("src/test/resources/test.txt");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doNothing().when(clamAvServiceMock).scan(any());

        FileTypeException exception = Assertions.assertThrows(FileTypeException.class, () -> sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files));
        Assertions.assertEquals(ErrorCode.FILE_TYPE_ERROR.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("400: with empty files should throw DocumentRequiredException")
    public void withEmptyFilesShouldThrowDocumentRequiredException() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        List<MultipartFile> files = new ArrayList<>();
        DocumentRequiredException exception = Assertions.assertThrows(DocumentRequiredException.class, () -> sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files));
        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(), exception.getErrorCode());
    }


    @Test
    @DisplayName("400: with null files should throw DocumentRequiredException")
    public void withNullFilesShouldThrowDocumentRequiredException() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        DocumentRequiredException exception = Assertions.assertThrows(DocumentRequiredException.class, () -> sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), null));
        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("404: with no submission present should return not found")
    public void withNoSubmissionReturnNotFound() {


        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity actual = sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_2, UUID.randomUUID(), null);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("500: with ioException should return 500")
    public void withIoExceptionShouldReturnInternalServerError() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        List<MultipartFile> files = new ArrayList<>();
        files.add(multipartFileMock);

        DocumentStorageException exception = Assertions.assertThrows(DocumentStorageException.class, () -> sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files));
        Assertions.assertEquals(ErrorCode.DOCUMENT_STORAGE_FAILURE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("502: with ioException should return 502")
    public void withScanFailureShouldReturnBadGateway() throws VirusDetectedException, IOException {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        File file = new File("src/test/resources/test.pdf");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doThrow(VirusDetectedException.class).when(clamAvServiceMock).scan(any());

        DocumentStorageException exception = Assertions.assertThrows(DocumentStorageException.class, () -> sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files));
        Assertions.assertEquals(ErrorCode.DOCUMENT_STORAGE_FAILURE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("403: without universalId should throw InvalidUniversalException")
    public void withoutUniversalIdShouldThrowInvalidUniversalException() throws VirusDetectedException, IOException {

        Map<String, Object> otherClaims = new HashMap<>();
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        File file = new File("src/test/resources/test.pdf");

        List<MultipartFile> files = new ArrayList<>();
        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));
        files.add(multipartFile);
        files.add(multipartFile);

        Mockito.doThrow(VirusDetectedException.class).when(clamAvServiceMock).scan(any());

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.uploadAdditionalSubmissionDocuments(TestHelpers.CASE_1, UUID.randomUUID(), files));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }

}
