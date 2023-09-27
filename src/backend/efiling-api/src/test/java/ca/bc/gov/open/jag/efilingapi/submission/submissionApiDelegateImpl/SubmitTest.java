package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitResponse;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingapi.error.InvalidUniversalException;
import ca.bc.gov.open.jag.efilingapi.error.PaymentException;
import ca.bc.gov.open.jag.efilingapi.error.SubmissionException;
import ca.bc.gov.open.jag.efilingapi.submission.SubmissionApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.GenerateUrlResponseMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.validator.GenerateUrlRequestValidator;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingPaymentException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.RushProcessing;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private ClamAvService clamAvServiceMock;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private GenerateUrlRequestValidator generateUrlRequestValidator;

    @Mock
    private Jwt jwtMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Submission submissionExists = Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
                .create();

        Mockito
                .doReturn(Optional.of(submissionExists))
                .when(submissionStoreMock).get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)));

        Submission submissionError = Submission
                .builder()
                .id(TestHelpers.CASE_2)
                .navigationUrls(TestHelpers.createNavigation(null, null, null))
                .filingPackage(
                        FilingPackage.builder()
                                .rush(RushProcessing.builder()
                                        .courtDate("2001-11-26T12:00:00Z")
                                        .create())
                                .create()
                )
                .create();

        Submission submissionPaymentError = Submission
                .builder()
                .id(TestHelpers.CASE_4)
                .navigationUrls(TestHelpers.createNavigation(null, null, null))
                .create();

        Mockito
                .doReturn(Optional.of(submissionError))
                .when(submissionStoreMock)
                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)));

        Mockito
                .doReturn(Optional.of(submissionPaymentError))
                .when(submissionStoreMock)
                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_4)));

        SubmitResponse result = new SubmitResponse();
        result.setPackageRef("packageref");

        Mockito
                .when(submissionServiceMock.createSubmission(Mockito.refEq(submissionExists), Mockito.any(), Mockito.any()))
                .thenReturn(result);

        Mockito.doThrow(EfilingSubmissionServiceException.class).when(submissionServiceMock).createSubmission(ArgumentMatchers.argThat(x -> x.getId().equals(TestHelpers.CASE_2)), Mockito.any(), Mockito.any());
        Mockito.doThrow(EfilingPaymentException.class).when(submissionServiceMock).createSubmission(ArgumentMatchers.argThat(x -> x.getId().equals(TestHelpers.CASE_4)), Mockito.any(), Mockito.any());

        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationPropertiesMock, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);

    }

    @Test
    @DisplayName("201: With valid request should return created and service id not early adopter")
    public void withUserHavingValidRequestShouldReturnCreated() {
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "early-adopters";
            }
        };

        Collection collection = new HashSet();
        collection.add(grantedAuthority);
        Mockito.when(authenticationMock.getAuthorities()).thenReturn(collection);
        Mockito.when(jwtMock.getClaim(Mockito.any())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);

        ResponseEntity<SubmitResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, null);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals("packageref", actual.getBody().getPackageRef());

    }

    @Test
    @DisplayName("201: With valid request should return created and service id early adopter")
    public void withUserHavingValidRequestEarlyAdopterShouldReturnCreated() {

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(jwtMock.getClaim(Mockito.any())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.setContext(securityContextMock);

        ResponseEntity<SubmitResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, null);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals("packageref", actual.getBody().getPackageRef());

    }

    @Test
    @DisplayName("500: with valid request but soap service throws an exception should throw SubmissionException")
    public void withErrorInServiceShouldThrowSubmissionException() {

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(jwtMock.getClaim(Mockito.any())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.setContext(securityContextMock);

        SubmissionException exception = Assertions.assertThrows(SubmissionException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_2, null));
        Assertions.assertEquals(ErrorCode.SUBMISSION_FAILURE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("400: with valid request but bambora is thrown and caught should throw PaymentException")
    public void withErrorInBamboraShouldThrowPaymentException() {

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(jwtMock.getClaim(Mockito.any())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.setContext(securityContextMock);

        PaymentException exception = Assertions.assertThrows(PaymentException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_4, null));
        Assertions.assertEquals(ErrorCode.PAYMENT_FAILURE.toString(), exception.getErrorCode());
    }

    @Test
    @DisplayName("404: with submission request that does not exist 404 should be returned")
    public void withSubmissionRequestThatDoesNotExist() {

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(jwtMock.getClaim(Mockito.any())).thenReturn(UUID.randomUUID().toString());
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.setContext(securityContextMock);

        ResponseEntity actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, null);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("403: with no universal id should throw InvalidUniversalException")
    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(jwtMock.getClaim(Mockito.any())).thenReturn(null);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.setContext(securityContextMock);

        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, null));
        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
    }
}
