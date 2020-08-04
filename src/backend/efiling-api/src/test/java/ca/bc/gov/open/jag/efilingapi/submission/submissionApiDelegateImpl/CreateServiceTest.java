package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateServiceResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class CreateServiceTest {
    private SubmissionApiDelegateImpl sut;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private GenerateUrlResponseMapper generateUrlResponseMapperMock;

    @Mock
    private NavigationProperties navigationPropertiesMock;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private AccountService accountServiceMock;


    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        Submission submissionExists = Submission
                .builder()
                .navigation(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_1), Mockito.any())).thenReturn(Optional.of(submissionExists));

        Submission submissionError = Submission
                .builder()
                .navigation(TestHelpers.createNavigation(null, null, null))
                .create();

        Mockito.when(submissionStoreMock.get(Mockito.eq(TestHelpers.CASE_2), Mockito.any())).thenReturn(Optional.of(submissionError));

        CreateServiceResponse result = new CreateServiceResponse();
        result.setServiceId(BigDecimal.TEN);

        Mockito.when(submissionServiceMock.createSubmission(Mockito.refEq(submissionExists))).thenReturn(result);

        Mockito.when(submissionServiceMock.createSubmission(Mockito.refEq(submissionError))).thenThrow(new EfilingSubmissionServiceException("Nooooooo", new Throwable()));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationPropertiesMock, submissionStoreMock, documentStoreMock);

    }

    @Test
    @DisplayName("201: With valid request should return created and service id")
    public void withUserHavingValidRequestShouldReturnOk() {

        ResponseEntity<CreateServiceResponse> actual = sut.createService(UUID.randomUUID(), TestHelpers.CASE_1, null);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(BigDecimal.TEN, actual.getBody().getServiceId());

    }

    @Test
    @DisplayName("500: with valid request but soap servie throws an exception return 500")
    public void withErrorInServiceShouldReturnInternalServiceError() {

        ResponseEntity<CreateServiceResponse> actual = sut.createService(UUID.randomUUID(), TestHelpers.CASE_2, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: with submission request that does not exist 404 should be returned")
    public void withSubmissionRequestThatDoesNotExist() {
        ResponseEntity<CreateServiceResponse> actual = sut.createService(UUID.randomUUID(), TestHelpers.CASE_3, null);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }
}
