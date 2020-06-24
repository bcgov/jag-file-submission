package ca.bc.gov.open.jag.efilingapi.submission;


import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountDetails;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingaccountclient.exception.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingapi.fee.models.FeeRequest;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.config.NavigationProperties;
import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentApiImpl Test Suite")
public class SubmissionApiDelegateImplTest {

    private static final String CASE_1 = "CASE1";
    private static final String CANCEL = "CANCEL";
    private static final String ERROR = "ERROR";
    private static final UUID TEST = UUID.randomUUID();
    private static final String TYPE = "TYPE";
    private static final String SUBTYPE = "SUBTYPE";
    private static final String URL = "http://doc.com";
    private static final String HEADER = "HEADER";
    public static final UUID CASE_5 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    public static final UUID CASE_6 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID CASE_7 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    public static final UUID CASE_8 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fc");
    public static final UUID CASE_9 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2f1");
    public static final String SUCCESS = "SUCCESS";
    private static final String CANCELURL = "CANCELURL";
    private static final String ERRORURL = "ERRORURL";
    private static final String SUCCESSURL = "SUCCESSURL";


    private SubmissionApiDelegateImpl sut;

    @Mock
    private NavigationProperties navigationProperties;

    @Mock
    private FeeService feeServiceMock;

    @Mock
    private SubmissionService submissionServiceMock;

    @Mock
    private SubmissionMapper submissionMapperMock;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private CacheProperties.Redis cachePropertiesredisMock;

    @Mock
    private EfilingAccountService efilingAccountServiceMock;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(cachePropertiesredisMock.getTimeToLive()).thenReturn(Duration.ofMillis(600000));
        when(cachePropertiesMock.getRedis()).thenReturn(cachePropertiesredisMock);
        when(navigationProperties.getBaseUrl()).thenReturn("https://httpbin.org/");
        when(feeServiceMock.getFee(any(FeeRequest.class))).thenReturn(new Fee(BigDecimal.TEN));

        DocumentProperties documentProperties = new DocumentProperties();
        List<String> efilingRole = new ArrayList<>();
        efilingRole.add("efiling");

        CsoAccountDetails csoAccountDetails = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, efilingRole);
        Submission submissionWithCsoAccount = new Submission(CASE_6, documentProperties, TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL), new Fee(BigDecimal.TEN), csoAccountDetails);

        when(submissionServiceMock.getByKey(Mockito.eq(CASE_5)))
                .thenReturn(Optional.empty());

        when(submissionServiceMock.getByKey(Mockito.eq(CASE_6)))
                .thenReturn(Optional.of(submissionWithCsoAccount));

        List<String> otherRole = new ArrayList<>();
        efilingRole.add("other");
        CsoAccountDetails csoAccountDetailsNoEfilingRole = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, otherRole);
        Submission submissionWithCsoAccountNoEfilingRole = new Submission(CASE_7, documentProperties, TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL), new Fee(BigDecimal.TEN), csoAccountDetailsNoEfilingRole);
        when(submissionServiceMock.getByKey(Mockito.eq(CASE_7)))
                .thenReturn(Optional.of(submissionWithCsoAccountNoEfilingRole));


        Submission submissionNoCsoAccount  = new Submission(CASE_8, documentProperties, TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL), new Fee(BigDecimal.TEN), null);
        when(submissionServiceMock.getByKey(Mockito.eq(CASE_8)))
                .thenReturn(Optional.of(submissionNoCsoAccount));

        sut = new SubmissionApiDelegateImpl(submissionServiceMock, navigationProperties, cachePropertiesMock, submissionMapperMock, feeServiceMock, efilingAccountServiceMock);

    }


    @Test
    @DisplayName("CASE1: when payload is valid")
    public void withValidPayloadShouldReturnOk() {

        when(submissionServiceMock.put(any())).thenReturn(Optional.of(Submission.builder().create()));
        List<String> efilingRole = new ArrayList<>();
        efilingRole.add("efiling");
        CsoAccountDetails csoAccountDetails = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, efilingRole);

        when(efilingAccountServiceMock.getAccountDetails(any())).thenReturn(csoAccountDetails);
        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR));

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().getEfilingUrl().startsWith("https://httpbin.org/"));
        assertNotNull(actual.getBody().getExpiryDate());
    }

    @Test
    @DisplayName("CASE2: when payload is valid but no efiling role return forbidden")
    public void withValidPayloadButNoRoleShouldReturnForbidden() {

        when(submissionServiceMock.put(any())).thenReturn(Optional.of(Submission.builder().create()));
        List<String> efilingRole = new ArrayList<>();
        efilingRole.add("NOTefiling");
        CsoAccountDetails csoAccountDetails = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, efilingRole);

        when(efilingAccountServiceMock.getAccountDetails(any())).thenReturn(csoAccountDetails);
        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR));

        ResponseEntity actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        assertEquals("INVROLE", ((EfilingError)actual.getBody()).getError());
        assertEquals("User does not have a valid role for this request.", ((EfilingError)actual.getBody()).getMessage());
        
    }


    @Test
    @DisplayName("With clientid having multiple account should return error")
    public void withClientIdHavingMultipleAccount() {

        when(submissionServiceMock.put(any())).thenReturn(Optional.empty());
        List<String> efilingRole = new ArrayList<>();
        efilingRole.add("efiling");
        CsoAccountDetails csoAccountDetails = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, efilingRole);

        when(efilingAccountServiceMock.getAccountDetails(Mockito.eq(CASE_9.toString()))).thenThrow(new CSOHasMultipleAccountException(CASE_9.toString()));
        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setUserId(CASE_9.toString());
        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESS, CANCEL, ERROR));

        ResponseEntity actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals("MLTACCNT", ((EfilingError)actual.getBody()).getError());
        assertEquals("Client has multiple CSO profiles", ((EfilingError)actual.getBody()).getMessage());

    }

    @Test
    @DisplayName("CASE3: when payload is valid but redis return nothing")
    public void withValidPayloadButRedisReturnNothingShouldReturnBadRequest() {

        when(submissionServiceMock.put(any())).thenReturn(Optional.empty());
        List<String> efilingRole = new ArrayList<>();
        efilingRole.add("efiling");
        CsoAccountDetails csoAccountDetails = new CsoAccountDetails(BigDecimal.TEN, BigDecimal.TEN, efilingRole);

        when(efilingAccountServiceMock.getAccountDetails(any())).thenReturn(csoAccountDetails);
        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
        generateUrlRequest.setNavigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR));

        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertNull(actual.getBody());
    }

    @Test
    @DisplayName("CASE4: with invalid guid should return badrequest")
    public void withValidIdReturnPayload() {
        Fee fee = new Fee(BigDecimal.TEN);

        Submission submission = Submission.builder().documentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE)).fee(fee).navigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR)).create();

        when(submissionServiceMock.getByKey(TEST)).thenReturn(Optional.of(submission));

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail("0");
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    @DisplayName("CASE5: with null redis storage response return NotFound")
    public void withNullRedisStorageResponseReturnNotFound() {

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail(CASE_5.toString());
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    @DisplayName("CASE6: with user having cso account and efiling role")
    public void withValidSubmissionIdShouldReturnAccountExistsAndHasEfilingRole() {

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail(CASE_6.toString());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().getCsoAccountExists());
        assertEquals(SUCCESSURL, actual.getBody().getNavigation().getSuccess().getUrl());
        assertEquals(CANCELURL, actual.getBody().getNavigation().getCancel().getUrl());
        assertEquals(ERRORURL, actual.getBody().getNavigation().getError().getUrl());
    }

    @Test
    @DisplayName("CASE7: with user having cso account and no efiling role")
    public void withValidSubmissionIdShouldReturnAccountExistsAndNoEfilingRole() {

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail(CASE_7.toString());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().getCsoAccountExists());
        assertEquals(SUCCESSURL, actual.getBody().getNavigation().getSuccess().getUrl());
        assertEquals(CANCELURL, actual.getBody().getNavigation().getCancel().getUrl());
        assertEquals(ERRORURL, actual.getBody().getNavigation().getError().getUrl());

    }

    @Test
    @DisplayName("CASE8: with user having cso account and no efiling role")
    public void withValidSubmissionButNoAccountShouldReturnFalse() {

        ResponseEntity<UserDetail> actual = sut.getSubmissionUserDetail(CASE_8.toString());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertFalse(actual.getBody().getCsoAccountExists());
        assertEquals(SUCCESSURL, actual.getBody().getNavigation().getSuccess().getUrl());
        assertEquals(CANCELURL, actual.getBody().getNavigation().getCancel().getUrl());
        assertEquals(ERRORURL, actual.getBody().getNavigation().getError().getUrl());

    }

}
