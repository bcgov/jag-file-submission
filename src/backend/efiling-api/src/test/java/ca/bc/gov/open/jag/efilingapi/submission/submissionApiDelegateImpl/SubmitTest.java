package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.joda.time.LocalDate;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("SubmissionApiDelegateImpl test suite")
public class SubmitTest {
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


    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        SubmitFilingPackageResponse result = new SubmitFilingPackageResponse();
        result.setAcknowledge(LocalDate.parse("2020-01-01"));
        result.setTransactionId(BigDecimal.TEN);
        Mockito.when(submissionServiceMock.submitFilingPackage(any(), Mockito.eq(TestHelpers.CASE_1), any())).thenReturn(result);
        Mockito.when(submissionServiceMock.submitFilingPackage(any(), Mockito.eq(TestHelpers.CASE_2), any())).thenThrow(new EfilingSubmissionServiceException("Nooooooo"));
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, generateUrlResponseMapperMock, navigationPropertiesMock, submissionStoreMock, documentStoreMock);

    }

    @Test
    @DisplayName("200: With user having cso account and efiling role return submission details")
    public void withUserHavingValidRequestShouldReturnOk() {

        ResponseEntity<SubmitFilingPackageResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, new SubmitFilingPackageRequest());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(BigDecimal.TEN, actual.getBody().getTransactionId());
        assertEquals(1, actual.getBody().getAcknowledge().getDayOfMonth());
        assertEquals(1, actual.getBody().getAcknowledge().getMonthOfYear());
        assertEquals(2020, actual.getBody().getAcknowledge().getYear());

    }

    @Test
    @DisplayName("500: With user having cso account and efiling role return submission details")
    public void withErrorInServiceShouldReturnInternalServiceError() {

        ResponseEntity<SubmitFilingPackageResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_2, new SubmitFilingPackageRequest());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());

    }
}
