package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class SubmitTest {
    private SubmitFilingPackageRequest errorRequest;
    private SubmitFilingPackageRequest request;
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

        SubmitFilingPackageResponse result = new SubmitFilingPackageResponse();
        result.setAcknowledge(LocalDate.parse("2020-01-01"));
        result.setTransactionId(BigDecimal.TEN);

        request = new SubmitFilingPackageRequest();
        Mockito.when(submissionServiceMock.createSubmission(Mockito.refEq(request), any())).thenReturn(result);

        errorRequest = new SubmitFilingPackageRequest();
        Mockito.when(submissionServiceMock.createSubmission(Mockito.refEq(errorRequest), any())).thenThrow(new EfilingSubmissionServiceException("Nooooooo", new Throwable()));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationPropertiesMock, submissionStoreMock, documentStoreMock);

    }

    @Test
    @DisplayName("200: With user having cso account and efiling role return submission details")
    public void withUserHavingValidRequestShouldReturnOk() {

        ResponseEntity<SubmitFilingPackageResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, request);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(BigDecimal.TEN, actual.getBody().getTransactionId());
        assertEquals(1, actual.getBody().getAcknowledge().getDayOfMonth());
        assertEquals(1, actual.getBody().getAcknowledge().getMonthOfYear());
        assertEquals(2020, actual.getBody().getAcknowledge().getYear());

    }

    @Test
    @DisplayName("500: With user having cso account and efiling role return submission details")
    public void withErrorInServiceShouldReturnInternalServiceError() {

        ResponseEntity<SubmitFilingPackageResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, errorRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: with submission request that does not exist 404 should be returned")
    public void withSubmissionRequestThatDoesNotExist() {
        ResponseEntity<SubmitFilingPackageResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, new SubmitFilingPackageRequest());
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }
}
