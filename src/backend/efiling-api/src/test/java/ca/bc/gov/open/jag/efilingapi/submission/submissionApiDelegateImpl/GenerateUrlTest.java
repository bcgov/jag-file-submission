package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.exception.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class GenerateUrlTest {

    private static final String HEADER = "test";
    private static final String URL = "http://doc";
    private static final String SUBTYPE = "subtype";
    private static final String TYPE = "type";
    private static final String SUCCESSURL = "http://success";
    private static final String CANCELURL = "http://cancel";
    private static final String ERRORURL = "http://error";

    private static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    private static final UUID CASE_2 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    private static final UUID CASE_3 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    private static final UUID CASE_4 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fc");
    private static final UUID CASE_5 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fd");


    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @BeforeAll
    public void setUp() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submission = Submission.builder().expiryDate(10).create();

        Mockito.when(submissionServiceMock.generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getUserId() == CASE_1)))
                .thenReturn(submission);

        Mockito.doThrow(new CSOHasMultipleAccountException("CSOHasMultipleAccountException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getUserId() == CASE_2));

        Mockito.doThrow(new InvalidAccountStateException("InvalidAccountStateException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getUserId() == CASE_3));

        Mockito.doThrow(new StoreException("StoreException message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getUserId() == CASE_4));

        Mockito.doThrow(new NestedEjbException_Exception("NestedEjbException_Exception message"))
                .when(submissionServiceMock).generateFromRequest(
                ArgumentMatchers.argThat(x -> x.getUserId() == CASE_5));

        // Testing the mapper part of this test
        generateUrlResponseMapperMock = new GenerateUrlResponseMapperImpl();

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock);

    }


    @Test
    @DisplayName("200: Valid request should generate a URL")
    public void withValidRequestShouldGenerateUrl() {

        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setUserId(CASE_1);
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL));

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertTrue(actual.getBody().getEfilingUrl().matches("http:\\/\\/localhost\\?submissionId=[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"));
        Assertions.assertNotNull(actual.getBody().getExpiryDate());

    }


    @Test
    @DisplayName("400: when CSOHasMultipleAccountException should return Bad Request")
    public void whenCSOHasMultipleAccountExceptionShouldReturnBadRequest() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setUserId(CASE_2);
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL));

        ResponseEntity actual = sut.generateUrl(generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.ACCOUNTEXCEPTION.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.ACCOUNTEXCEPTION.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("403: when InvalidAccountStateException should return FORBIDDEN")
    public void whenInvalidAccountStateExceptionShouldReturnForbidden() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setUserId(CASE_3);
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL));

        ResponseEntity actual = sut.generateUrl(generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.INVALIDROLE.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.INVALIDROLE.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("500: when StoreException should return INTERNAL SERVER ERROR")
    public void whenStoreExceptionShouldReturnInternalServerError() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setUserId(CASE_4);
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL));

        ResponseEntity actual = sut.generateUrl(generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.CACHE_ERROR.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.CACHE_ERROR.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("500: when NestedEjbException_Exception should return INTERNAL SERVER ERROR")
    public void whenNestedEjbException_ExceptionShouldReturnInternalServerError() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setUserId(CASE_5);
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL));

        ResponseEntity actual = sut.generateUrl(generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.GETPROFILESEXCEPTION.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.GETPROFILESEXCEPTION.getErrorMessage(), actualError.getMessage());

    }
}
