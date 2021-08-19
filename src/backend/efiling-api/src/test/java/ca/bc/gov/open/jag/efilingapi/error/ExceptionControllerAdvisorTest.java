package ca.bc.gov.open.jag.efilingapi.error;

import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ExceptionControllerAdvisor test suite")
public class ExceptionControllerAdvisorTest {

    public ExceptionControllerAdvisor sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);
        sut = new ExceptionControllerAdvisor();

    }

    @Test
    @DisplayName("500: Assert Cache Exception")
    public void testCacheException() {

        String expected = "Something went wrong";

        //arrange
        CacheException exception = new CacheException(expected);

        //act
        ResponseEntity<Object> result = sut.handleCacheException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.CACHE_ERROR.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("500: Assert Court Location Exception")
    public void testCourtLocationException() {

        String expected = "Something went wrong";

        //arrange
        CourtLocationException exception = new CourtLocationException(expected);

        //act
        ResponseEntity<Object> result = sut.handleCourtLocationException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.COURT_LOCATION_ERROR.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("500: Assert Create Account Exception")
    public void testCreateAccountException() {

        String expected = "Something went wrong";

        //arrange
        CreateAccountException exception = new CreateAccountException(expected);

        //act
        ResponseEntity<Object> result = sut.handleCreateAccountException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.CREATE_ACCOUNT_EXCEPTION.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("404: Assert Delete Document Exception Returns Not Found")
    public void testDeleteDocumentExceptionReturnNotFound() {

        String expected = "Something went wrong";

        //arrange
        DeleteDocumentException exception = new DeleteDocumentException(expected, HttpStatus.NOT_FOUND);

        //act
        ResponseEntity<Object> result = sut.handleDeleteDocumentException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.DELETE_DOCUMENT_ERROR.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert Delete Document Exception Returns Bad Request")
    public void testDeleteDocumentExceptionReturnsBadRequest() {

        String expected = "Something went wrong";

        //arrange
        DeleteDocumentException exception = new DeleteDocumentException(expected, HttpStatus.BAD_REQUEST);

        //act
        ResponseEntity<Object> result = sut.handleDeleteDocumentException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.DELETE_DOCUMENT_ERROR.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert Document Required Exception")
    public void testDocumentRequiredException() {

        String expected = "Something went wrong";

        //arrange
        DocumentRequiredException exception = new DocumentRequiredException(expected);

        //act
        ResponseEntity<Object> result = sut.handleDocumentRequiredException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("502: Assert Document Storage Exception")
    public void testDocumentStorageException() {

        String expected = "Something went wrong";

        //arrange
        DocumentStorageException exception = new DocumentStorageException(expected);

        //act
        ResponseEntity<Object> result = sut.handleDocumentStorageException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.DOCUMENT_STORAGE_FAILURE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("500: Assert Document Type Exception")
    public void testDocumentTypeException() {

        String expected = "Something went wrong";

        //arrange
        DocumentTypeException exception = new DocumentTypeException(expected);

        //act
        ResponseEntity<Object> result = sut.handleDocumentTypeException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.DOCUMENT_TYPE_ERROR.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert File Type Exception")
    public void testFileTypeException() {

        String expected = "Something went wrong";

        //arrange
        FileTypeException exception = new FileTypeException(expected);

        //act
        ResponseEntity<Object> result = sut.handleFileTypeException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.FILE_TYPE_ERROR.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }


    @Test
    @DisplayName("404: Assert Filing Package Not Found Exception")
    public void testFilingPackageNotFoundException() {

        String expected = "Something went wrong";

        //arrange
        FilingPackageNotFoundException exception = new FilingPackageNotFoundException(expected);

        //act
        ResponseEntity<Object> result = sut.handleFilingPackageNotFoundException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert Invalid Initial Submission Payload Exception")
    public void testInvalidInitialSubmissionPayloadException() {

        String expected = "Something went wrong";
        String expectedDetail1 = "Detail 1 - FileNumber [3] does not exists.";
        String expectedDetail2 = "Detail 2 - FileNumber [3] does not exists.";
        List<String> expectedDetails = Arrays.asList(expectedDetail1, expectedDetail2);

        //arrange
        InvalidInitialSubmissionPayloadException exception = new InvalidInitialSubmissionPayloadException(expected, expectedDetails);

        //act
        ResponseEntity<Object> result = sut.handleInvalidInitialSubmissionPayloadException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.INVALID_INITIAL_SUBMISSION_PAYLOAD.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());
        Assertions.assertEquals(expectedDetails, ((EfilingError) result.getBody()).getDetails());
        Assertions.assertEquals(expectedDetail1, ((EfilingError) result.getBody()).getDetails().get(0));
        Assertions.assertEquals(expectedDetail2, ((EfilingError) result.getBody()).getDetails().get(1));

    }

    @Test
    @DisplayName("403: Assert Invalid Role Exception")
    public void testInvalidRoleException() {

        String expected = "Something went wrong";

        //arrange
        InvalidRoleException exception = new InvalidRoleException(expected);

        //act
        ResponseEntity<Object> result = sut.handleInvalidRoleException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.INVALIDROLE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("403: Assert Invalid Universal Exception")
    public void testInvalidUniversalException() {

        String expected = "Something went wrong";

        //arrange
        InvalidUniversalException exception = new InvalidUniversalException(expected);

        //act
        ResponseEntity<Object> result = sut.handleInvalidUniversalException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("403: Assert Missing Application Code Exception")
    public void testMissingApplicationCodeException() {

        String expected = "Something went wrong";

        //arrange
        MissingApplicationCodeException exception = new MissingApplicationCodeException(expected);

        //act
        ResponseEntity<Object> result = sut.handleMissingApplicationCodeException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.MISSING_APPLICATION_CODE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("403: Assert Missing Identity Provider Exception")
    public void testMissingIdentityProviderException() {

        String expected = "Something went wrong";

        //arrange
        MissingIdentityProviderException exception = new MissingIdentityProviderException(expected);

        //act
        ResponseEntity<Object> result = sut.handleMissingIdentityProviderException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.MISSING_IDENTITY_PROVIDER.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("403: Assert Missing Universal Id Exception")
    public void testMissingUniversalIdException() {

        String expected = "Something went wrong";

        //arrange
        MissingUniversalIdException exception = new MissingUniversalIdException(expected);

        //act
        ResponseEntity<Object> result = sut.handleMissingUniversalIdException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert Multiple Accounts Exception")
    public void testMultipleAccountsException() {

        String expected = "Something went wrong";

        //arrange
        AccountException exception = new AccountException(expected);

        //act
        ResponseEntity<Object> result = sut.handleAccountException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.ACCOUNTEXCEPTION.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert Payment Exception")
    public void testPaymentException() {

        String expected = "Something went wrong";

        //arrange
        PaymentException exception = new PaymentException(expected);

        //act
        ResponseEntity<Object> result = sut.handlePaymentException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.PAYMENT_FAILURE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("500: Assert Submission Exception")
    public void testSubmissionException() {

        String expected = "Something went wrong";

        //arrange
        SubmissionException exception = new SubmissionException(expected);

        //act
        ResponseEntity<Object> result = sut.handleSubmissionException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.SUBMISSION_FAILURE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("500: Assert Update Client Exception")
    public void testUpdateClientException() {

        String expected = "Something went wrong";

        //arrange
        UpdateClientException exception = new UpdateClientException(expected);

        //act
        ResponseEntity<Object> result = sut.handleUpdateClientException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.UPDATE_CLIENT_EXCEPTION.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("500: Assert Url Generation Exception")
    public void testUrlGenerationException() {

        String expected = "Something went wrong";

        //arrange
        UrlGenerationException exception = new UrlGenerationException(expected);

        //act
        ResponseEntity<Object> result = sut.handleUrlGenerationException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.URL_GENERATION_FAILURE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

    @Test
    @DisplayName("400: Assert No Registry Notice Exception")
    public void testNoRegistryNoticeException() {

        String expected = "Something went wrong";

        //arrange
        NoRegistryNoticeException exception = new NoRegistryNoticeException(expected);

        //act
        ResponseEntity<Object> result = sut.handleNoRegistryNoticeException(exception);

        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals(ErrorCode.MISSING_REGISTRY_NOTICE.toString(),
                ((EfilingError) Objects.requireNonNull(result.getBody())).getError());
        Assertions.assertEquals(expected, ((EfilingError) result.getBody()).getMessage());

    }

}
