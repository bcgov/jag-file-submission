package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

// FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("SubmissionApiDelegateImpl test suite")
public class SubmitTest {
//    private SubmissionApiDelegateImpl sut;
//
//    @Mock
//    private SubmissionService submissionServiceMock;
//
//    @Mock
//    private GenerateUrlResponseMapper generateUrlResponseMapperMock;
//
//    @Mock
//    private NavigationProperties navigationPropertiesMock;
//
//    @Mock
//    private SubmissionStore submissionStoreMock;
//
//    @Mock
//    private DocumentStore documentStoreMock;
//
//    @Mock
//    private AccountService accountServiceMock;
//
//    @Mock
//    private ClamAvService clamAvServiceMock;
//
//    @Mock
//    private SecurityContext securityContextMock;
//
//    @Mock
//    private Authentication authenticationMock;
//
//    @Mock
//    private KeycloakPrincipal keycloakPrincipalMock;
//
//    @Mock
//    private KeycloakSecurityContext keycloakSecurityContextMock;
//
//    @Mock
//    private AccessToken tokenMock;
//
//    @Mock
//    private GenerateUrlRequestValidator generateUrlRequestValidator;
//
//    @Mock
//    private AccessToken.Access resourceAccessMock;
//
//    @BeforeAll
//    public void setUp() {
//
//        MockitoAnnotations.openMocks(this);
//
//        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
//        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
//        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
//        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);
//
//        SecurityContextHolder.setContext(securityContextMock);
//
//        Submission submissionExists = Submission
//                .builder()
//                .id(TestHelpers.CASE_1)
//                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
//                .create();
//
//        Mockito
//                .doReturn(Optional.of(submissionExists))
//                .when(submissionStoreMock).get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)));
//
//        Submission submissionError = Submission
//                .builder()
//                .id(TestHelpers.CASE_2)
//                .navigationUrls(TestHelpers.createNavigation(null, null, null))
//                .filingPackage(
//                        FilingPackage.builder()
//                                .rush(RushProcessing.builder()
//                                        .courtDate("2001-11-26T12:00:00Z")
//                                        .create())
//                                .create()
//                )
//                .create();
//
//        Submission submissionPaymentError = Submission
//                .builder()
//                .id(TestHelpers.CASE_4)
//                .navigationUrls(TestHelpers.createNavigation(null, null, null))
//                .create();
//
//        Mockito
//                .doReturn(Optional.of(submissionError))
//                .when(submissionStoreMock)
//                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)));
//
//        Mockito
//                .doReturn(Optional.of(submissionPaymentError))
//                .when(submissionStoreMock)
//                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_4)));
//
//        SubmitResponse result = new SubmitResponse();
//        result.setPackageRef("packageref");
//
//        Mockito
//                .when(submissionServiceMock.createSubmission(Mockito.refEq(submissionExists), Mockito.any(), Mockito.any()))
//                .thenReturn(result);
//
//        Mockito.doThrow(EfilingSubmissionServiceException.class).when(submissionServiceMock).createSubmission(ArgumentMatchers.argThat(x -> x.getId().equals(TestHelpers.CASE_2)), Mockito.any(), Mockito.any());
//        Mockito.doThrow(EfilingPaymentException.class).when(submissionServiceMock).createSubmission(ArgumentMatchers.argThat(x -> x.getId().equals(TestHelpers.CASE_4)), Mockito.any(), Mockito.any());
//
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationPropertiesMock, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
//
//    }
//
//    @Test
//    @DisplayName("201: With valid request should return created and service id not early adopter")
//    public void withUserHavingValidRequestShouldReturnCreated() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(resourceAccessMock.isUserInRole(ArgumentMatchers.eq("early-adopters"))).thenReturn(true);
//        Mockito.when(tokenMock.getResourceAccess(ArgumentMatchers.eq(Keys.EFILING_API_NAME))).thenReturn(resourceAccessMock);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<SubmitResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, null);
//        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
//        assertEquals("packageref", actual.getBody().getPackageRef());
//
//    }
//
//    @Test
//    @DisplayName("201: With valid request should return created and service id early adopter")
//    public void withUserHavingValidRequestEarlyAdopterShouldReturnCreated() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<SubmitResponse> actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_1, null);
//        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
//        assertEquals("packageref", actual.getBody().getPackageRef());
//
//    }
//
//    @Test
//    @DisplayName("500: with valid request but soap service throws an exception should throw SubmissionException")
//    public void withErrorInServiceShouldThrowSubmissionException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        SubmissionException exception = Assertions.assertThrows(SubmissionException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_2, null));
//        Assertions.assertEquals(ErrorCode.SUBMISSION_FAILURE.toString(), exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("400: with valid request but bambora is thrown and caught should throw PaymentException")
//    public void withErrorInBamboraShouldThrowPaymentException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        PaymentException exception = Assertions.assertThrows(PaymentException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_4, null));
//        Assertions.assertEquals(ErrorCode.PAYMENT_FAILURE.toString(), exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("404: with submission request that does not exist 404 should be returned")
//    public void withSubmissionRequestThatDoesNotExist() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity actual = sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, null);
//        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("403: with no universal id should throw InvalidUniversalException")
//    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY,null);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.submit(UUID.randomUUID(), TestHelpers.CASE_3, null));
//        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
//    }
}
