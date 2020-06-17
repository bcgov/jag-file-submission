package ca.bc.gov.open.jagefilingapi.submission;


import ca.bc.gov.open.jagefilingapi.api.model.*;
import ca.bc.gov.open.jagefilingapi.config.NavigationProperties;
import ca.bc.gov.open.jagefilingapi.fee.FeeService;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import ca.bc.gov.open.jagefilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jagefilingapi.submission.models.Submission;
import ca.bc.gov.open.jagefilingapi.submission.service.SubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentApiImpl Test Suite")
public class SubmissionApiImplTest {

    private static final String CASE_1 = "CASE1";
    private static final String CANCEL = "CANCEL";
    private static final String ERROR = "ERROR";
    private static final UUID TEST = UUID.randomUUID();
    private static final String TYPE = "TYPE";
    private static final String SUBTYPE = "SUBTYPE";
    private static final String URL = "http://doc.com";
    private static final String HEADER = "HEADER";

    @InjectMocks
    private SubmissionApiImpl sut;

    @Mock
    NavigationProperties navigationProperties;

    @Mock
    FeeService feeService;

    @Mock
    SubmissionService submissionServiceMock;

    @Mock
    SubmissionMapper submissionMapperMock;

    @Mock
    CacheProperties cachePropertiesMock;

    @Mock
    CacheProperties.Redis cachePropertiesredisMock;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(cachePropertiesredisMock.getTimeToLive()).thenReturn(Duration.ofMillis(600000));
        when(cachePropertiesMock.getRedis()).thenReturn(cachePropertiesredisMock);
        when(navigationProperties.getBaseUrl()).thenReturn("https://httpbin.org/");
        when(feeService.getFee(any(FeeRequest.class))).thenReturn(new Fee(BigDecimal.TEN));
    }


    @Test
    @DisplayName("CASE1: when payload is valid")
    @Order(1)
    public void withValidPayloadShouldReturnOk() {

        when(submissionServiceMock.put(any())).thenReturn(Optional.of(Submission.builder().create()));

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(createDocumentProperties());
        generateUrlRequest.setNavigation(createNavigation());

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().getEFilingUrl().startsWith("https://httpbin.org/"));
        assertNotNull(actual.getBody().getExpiryDate());
    }

    @Test
    @DisplayName("CASE2: when payload is valid but redis return nothing")
    @Order(2)
    public void withValidPayloadButRedisReturnNothingReturnBadRequest() {

        when(submissionServiceMock.put(any())).thenReturn(Optional.empty());

        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(createDocumentProperties());
        generateUrlRequest.setNavigation(createNavigation());

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertNull(actual.getBody());
    }

    @Test
    @DisplayName("CASE3: with validId return payload")
    @Order(3)
    public void withValidIdReturnPayload() {
        Fee fee = new Fee(BigDecimal.TEN);

        Submission submission = Submission.builder().documentProperties(createDocumentProperties()).fee(fee).navigation(createNavigation()).create();

        when(submissionServiceMock.getByKey(TEST)).thenReturn(Optional.of(submission));

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail("0");
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().getCsoAccountExists());

    }

    @Test
    @DisplayName("CASE4: with null redis storage response return NotFound")
    @Order(4)
    public void withNullRedisStorageResponseReturnNotFound() {

        when(submissionServiceMock.getByKey(any()))
                .thenReturn(Optional.empty());

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail("1");
        assertFalse(actual.getBody().getCsoAccountExists());

    }

    private DocumentProperties createDocumentProperties() {
        DocumentProperties documentProperties = new DocumentProperties();
        EndpointAccess documentAccess = new EndpointAccess();
        documentAccess.setHeaders(Collections.singletonMap(HEADER, HEADER));
        documentAccess.setUrl(URL);
        documentAccess.setVerb(EndpointAccess.VerbEnum.POST);
        documentProperties.setSubmissionAccess(documentAccess);
        documentProperties.setSubType(SUBTYPE);
        documentProperties.setType(TYPE);
        return documentProperties;
    }

    private Navigation createNavigation() {
        Navigation navigation = new Navigation();
        Redirect successRedirect = new Redirect();
        successRedirect.setUrl(CASE_1);
        navigation.setSuccess(successRedirect);
        Redirect cancelRedirect = new Redirect();
        cancelRedirect.setUrl(CANCEL);
        navigation.setCancel(cancelRedirect);
        Redirect errorRedirect = new Redirect();
        errorRedirect.setUrl(ERROR);
        navigation.setError(errorRedirect);
        return navigation;
    }
}
