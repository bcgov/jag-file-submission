package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
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

    private static final String TYPE = "type";
    private static final String DISPLAYNAME = "DISPLAYNAME";


    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private DocumentStore documentStoreMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        NavigationProperties navigationProperties = new NavigationProperties();
        navigationProperties.setBaseUrl("http://localhost");

        Submission submission = Submission.builder().id(TestHelpers.CASE_1).owner(TestHelpers.CASE_2).expiryDate(10).create();

        Mockito.when(submissionServiceMock.generateFromRequest(
                Mockito.any(),
                Mockito.eq(TestHelpers.CASE_1),
                Mockito.any()))
                .thenReturn(submission);

        Mockito.doThrow(new CSOHasMultipleAccountException("CSOHasMultipleAccountException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                Mockito.eq(TestHelpers.CASE_2),
                Mockito.any());

        Mockito.doThrow(new InvalidAccountStateException("InvalidAccountStateException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                Mockito.eq(TestHelpers.CASE_3),
                Mockito.any());

        Mockito.doThrow(new StoreException("StoreException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                Mockito.eq(TestHelpers.CASE_4),
                Mockito.any());

        Mockito.doThrow(new EfilingDocumentServiceException("EfilingDocumentServiceException message"))
                .when(submissionServiceMock).generateFromRequest(
                Mockito.any(),
                Mockito.eq(TestHelpers.CASE_5),
                Mockito.any());

        // Testing the mapper part of this test
        generateUrlResponseMapperMock = new GenerateUrlResponseMapperImpl();

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock);

    }


    @Test
    @DisplayName("200: Valid request should generate a URL")
    public void withValidRequestShouldGenerateUrl() {

        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(TestHelpers.createClientApplication(DISPLAYNAME,TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(UUID.randomUUID(), TestHelpers.CASE_1, generateUrlRequest);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals("http://localhost?submissionId=" + TestHelpers.CASE_1.toString() + "&temp=" + TestHelpers.CASE_2 , actual.getBody().getEfilingUrl());
        Assertions.assertNotNull(actual.getBody().getExpiryDate());

    }


    @Test
    @DisplayName("400: when CSOHasMultipleAccountException should return Bad Request")
    public void whenCSOHasMultipleAccountExceptionShouldReturnBadRequest() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(TestHelpers.createClientApplication(DISPLAYNAME,TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), TestHelpers.CASE_2, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.ACCOUNTEXCEPTION.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.ACCOUNTEXCEPTION.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("403: when InvalidAccountStateException should return FORBIDDEN")
    public void whenInvalidAccountStateExceptionShouldReturnForbidden() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(TestHelpers.createClientApplication(DISPLAYNAME,TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), TestHelpers.CASE_3, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.INVALIDROLE.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.INVALIDROLE.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("500: when StoreException should return INTERNAL SERVER ERROR")
    public void whenStoreExceptionShouldReturnInternalServerError() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(TestHelpers.createClientApplication(DISPLAYNAME,TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), TestHelpers.CASE_4, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.CACHE_ERROR.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.CACHE_ERROR.getErrorMessage(), actualError.getMessage());
    }

    @Test
    @DisplayName("500: when DocumentException should return INTERNAL SERVER ERROR")
    public void whenDocumentExceptionShouldReturnInternalServerError() {
        @Valid GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(TestHelpers.createClientApplication(DISPLAYNAME,TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL));

        ResponseEntity actual = sut.generateUrl(UUID.randomUUID(), TestHelpers.CASE_5, generateUrlRequest);

        EfilingError actualError = (EfilingError) actual.getBody();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode(), actualError.getError());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage(), actualError.getMessage());
    }

}
