package ca.bc.gov.open.jag.efilingapi.submission;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentApiImpl Test Suite")
public class SubmissionApiDelegateImplTest {

//    private static final String CASE_1 = "CASE1";
//    private static final String CANCEL = "CANCEL";
//    private static final String ERROR = "ERROR";
//    private static final UUID TEST = UUID.randomUUID();
//    private static final String TYPE = "TYPE";
//    private static final String SUBTYPE = "SUBTYPE";
//    private static final String URL = "http://doc.com";
//    private static final String HEADER = "HEADER";
//    public static final UUID CASE_5 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
//    public static final UUID CASE_6 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
//    public static final UUID CASE_7 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
//    public static final UUID CASE_8 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fc");
//    public static final UUID CASE_9 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2f1");
//    public static final UUID CASE_10 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2f2");
//    public static final String SUCCESS = "SUCCESS";
//    private static final String CANCELURL = "CANCELURL";
//    private static final String ERRORURL = "ERRORURL";
//    private static final String SUCCESSURL = "SUCCESSURL";
//
//
//    private SubmissionApiDelegateImpl sut;
//
//    @Mock
//    private NavigationProperties navigationProperties;
//
//    @Mock
//    private FeeService feeServiceMock;
//
//    @Mock
//    private SubmissionStore submissionStoreMock;
//
//    @Mock
//    private SubmissionMapper submissionMapperMock;
//
//    @Mock
//    private CacheProperties cachePropertiesMock;
//
//    @Mock
//    private CacheProperties.Redis cachePropertiesredisMock;
//
//    @Mock
//    private EfilingAccountService efilingAccountServiceMock;
//
//    @Mock
//    private EfilingLookupService efilingLookupServiceMock;
//
//    @BeforeAll
//    public void setUp() throws DatatypeConfigurationException {
//        MockitoAnnotations.initMocks(this);
//
//        when(cachePropertiesredisMock.getTimeToLive()).thenReturn(Duration.ofMillis(600000));
//        when(cachePropertiesMock.getRedis()).thenReturn(cachePropertiesredisMock);
//        when(navigationProperties.getBaseUrl()).thenReturn("https://httpbin.org/");
//        when(feeServiceMock.getFee(any(FeeRequest.class))).thenReturn(new Fee(BigDecimal.TEN));
//
//        DocumentProperties documentProperties = new DocumentProperties();
//
//        AccountDetails accountDetails = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "firstNameCase6", "lastNameCase6", "middleNameCase6",  "emailCase6");
//        Submission submissionWithCsoAccount = new Submission(CASE_6, documentProperties, TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL), new Fee(BigDecimal.TEN), accountDetails);
//
//        when(submissionStoreMock.getByKey(Mockito.eq(CASE_5)))
//                .thenReturn(Optional.empty());
//
//        when(submissionStoreMock.getByKey(Mockito.eq(CASE_6)))
//                .thenReturn(Optional.of(submissionWithCsoAccount));
//
//        AccountDetails accountDetailsNoEfilingRole = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, false, "firstName", "lastName", "middleName",  "email");
//        Submission submissionWithCsoAccountNoEfilingRole = new Submission(CASE_7, documentProperties, TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL), new Fee(BigDecimal.TEN), accountDetailsNoEfilingRole);
//        when(submissionStoreMock.getByKey(Mockito.eq(CASE_7)))
//                .thenReturn(Optional.of(submissionWithCsoAccountNoEfilingRole));
//
//
//        Submission submissionNoCsoAccount  = new Submission(CASE_8, documentProperties, TestHelpers.createNavigation(SUCCESSURL, CANCELURL, ERRORURL), new Fee(BigDecimal.TEN), null);
//        when(submissionStoreMock.getByKey(Mockito.eq(CASE_8)))
//                .thenReturn(Optional.of(submissionNoCsoAccount));
//
//        ServiceFee serviceFee = new ServiceFee();
//        serviceFee.setFeeAmt(BigDecimal.valueOf(2));
//        when(efilingLookupServiceMock.getServiceFee(any())).thenReturn(serviceFee);
//        sut = new SubmissionApiDelegateImpl(submissionStoreMock, submissionStore, navigationProperties, cachePropertiesMock, submissionMapperMock, efilingAccountServiceMock, efilingLookupServiceMock, feeServiceMock, submissionStore);
//
//    }
//
//
//    @Test
//    @DisplayName("CASE1: when payload is valid")
//    public void withValidPayloadShouldReturnOk() throws NestedEjbException_Exception {
//
//        when(submissionStoreMock.put(any())).thenReturn(Optional.of(Submission.builder().create()));
//        AccountDetails accountDetails = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "firstName", "lastName", "middleName",  "email");
//
//        when(efilingAccountServiceMock.getAccountDetails(any())).thenReturn(accountDetails);
//        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
//
//        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
//        generateUrlRequest.setNavigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR));
//
//        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);
//
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertTrue(actual.getBody().getEfilingUrl().startsWith("https://httpbin.org/"));
//        assertNotNull(actual.getBody().getExpiryDate());
//    }
//
//    @Test
//    @DisplayName("CASE2: when payload is valid but no efiling role return forbidden")
//    public void withValidPayloadButNoRoleShouldReturnForbidden() throws NestedEjbException_Exception {
//
//        when(submissionStoreMock.put(any())).thenReturn(Optional.of(Submission.builder().create()));
//        AccountDetails accountDetails = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, false, "firstName", "lastName", "middleName",  "email");
//
//        when(efilingAccountServiceMock.getAccountDetails(any())).thenReturn(accountDetails);
//        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
//
//        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
//        generateUrlRequest.setNavigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR));
//
//        ResponseEntity actual = sut.generateUrl(generateUrlRequest);
//
//        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
//        assertEquals("INVROLE", ((EfilingError)actual.getBody()).getError());
//        assertEquals("User does not have a valid role for this request.", ((EfilingError)actual.getBody()).getMessage());
//
//    }
//
//
//    @Test
//    @DisplayName("With clientid having multiple account should return error")
//    public void withClientIdHavingMultipleAccount() throws NestedEjbException_Exception {
//
//        when(submissionStoreMock.put(any())).thenReturn(Optional.empty());
//        AccountDetails accountDetails = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "firstName", "lastName", "middleName", "email");
//
//        when(efilingAccountServiceMock.getAccountDetails(Mockito.eq(CASE_9.toString()))).thenThrow(new CSOHasMultipleAccountException(CASE_9.toString()));
//        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
//
//        generateUrlRequest.setUserId(CASE_9.toString());
//        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
//        generateUrlRequest.setNavigation(TestHelpers.createNavigation(SUCCESS, CANCEL, ERROR));
//
//        ResponseEntity actual = sut.generateUrl(generateUrlRequest);
//
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
//        assertEquals("MLTACCNT", ((EfilingError)actual.getBody()).getError());
//        assertEquals("Client has multiple CSO profiles", ((EfilingError)actual.getBody()).getMessage());
//
//    }
//
//
//    @Test
//    @DisplayName("CASE3: when payload is valid but redis return nothing")
//    public void withValidPayloadButRedisReturnNothingShouldReturnBadRequest() throws NestedEjbException_Exception, DatatypeConfigurationException {
//
//        when(submissionStoreMock.put(any())).thenReturn(Optional.empty());
//        AccountDetails accountDetails = new AccountDetails(BigDecimal.TEN, BigDecimal.TEN, true, "firstName", "lastName", "middleName",  "email");
//
//        when(efilingAccountServiceMock.getAccountDetails(any())).thenReturn(accountDetails);
//        GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest();
//
//        generateUrlRequest.setDocumentProperties(TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE));
//        generateUrlRequest.setNavigation(TestHelpers.createNavigation(CASE_1, CANCEL, ERROR));
//
//        ResponseEntity<GenerateUrlResponse> actual = sut.generateUrl(generateUrlRequest);
//
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
//        assertNull(actual.getBody());
//    }


}
