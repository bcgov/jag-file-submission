package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("SubmissionApiDelegateImpl test suite")
public class GetSubmissionTest {
//
//
//    private static final String SERVICE_TYPE_CD = "DCFL";
//    private static final String SERVICE_TYPE_CD1 = "NOTDCFL";
//    private static final String INTERNAL_CLIENT_NUMBER = "123";
//    private static final String IDENTITY_PROVIDER = "identity_provider_alias";
//
//    private SubmissionApiDelegateImpl sut;
//
//    @Mock
//    private SubmissionService submissionServiceMock;
//
//    @Mock
//    private GenerateUrlResponseMapper generateUrlResponseMapperMock;
//
//    @Mock
//    private SubmissionStore submissionStoreMock;
//
//    @Mock
//    private DocumentStore documentStoreMock;
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
//    private AccountService accountServiceMock;
//
//    @Mock
//    private ClamAvService clamAvServiceMock;
//
//    @Mock
//    private GenerateUrlRequestValidator generateUrlRequestValidator;
//
//    @BeforeAll
//    public void beforeAll() {
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
//        NavigationProperties navigationProperties = new NavigationProperties();
//        navigationProperties.setBaseUrl("http://localhost");
//        Submission submissionWithCsoAccount = Submission
//                .builder()
//                .clientAppName(TestHelpers.DESCRIPTION)
//                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
//                .create();
//
//        Mockito
//                .doReturn(Optional.of(submissionWithCsoAccount))
//                .when(submissionStoreMock)
//                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_2)));
//
//        Submission submissionWithoutCsoAccount = Submission
//                .builder()
//                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
//                .create();
//
//        Mockito
//                .doReturn(Optional.of(submissionWithoutCsoAccount))
//                .when(submissionStoreMock)
//                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_3)));
//
//        Mockito
//                .doReturn(Optional.of(submissionWithoutCsoAccount))
//                .when(submissionStoreMock)
//                .get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_4)));
//
//
//        Mockito.when(accountServiceMock.getCsoAccountDetails(Mockito.eq(TestHelpers.CASE_2_STRING)))
//                .thenReturn(AccountDetails
//                        .builder()
//                        .clientId(BigDecimal.TEN)
//                        .internalClientNumber(INTERNAL_CLIENT_NUMBER)
//                        .universalId(TestHelpers.CASE_2_STRING)
//                        .accountId(BigDecimal.TEN)
//                        .fileRolePresent(true)
//                        .cardRegistered(true)
//                        .create());
//
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
//
//    }
//
//    @Test
//    @DisplayName("404: With null redis storage response return NotFound")
//    public void withNullRedisStorageResponseReturnNotFound() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<GetSubmissionConfigResponse> actual = sut.getSubmissionConfig(
//                UUID.randomUUID(), 
//                TestHelpers.CASE_1);
//        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("200: With user having cso account and efiling role return submission details")
//    public void withUserHavingCsoAccountShouldReturnUserDetailsAndAccount() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_2);
//        otherClaims.put(Keys.IDENTITY_PROVIDER_CLAIM_KEY, IDENTITY_PROVIDER);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<GetSubmissionConfigResponse> actual = sut.getSubmissionConfig(UUID.fromString(
//                TestHelpers.CASE_2_STRING), 
//                TestHelpers.CASE_2);
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(TestHelpers.SUCCESS_URL, actual.getBody().getNavigationUrls().getSuccess());
//        assertEquals(TestHelpers.CANCEL_URL, actual.getBody().getNavigationUrls().getCancel());
//        assertEquals(TestHelpers.ERROR_URL, actual.getBody().getNavigationUrls().getError());
//        assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getClientAppName());
//
//    }
//
//    @Test
//    @DisplayName("200: With user not having cso account")
//    public void withUserHavingNoCsoAccountShouldReturnUserDetailsButNoAccount() {
//
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<GetSubmissionConfigResponse> actual = sut.getSubmissionConfig(
//                TestHelpers.CASE_3, 
//                UUID.randomUUID());
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(TestHelpers.SUCCESS_URL, actual.getBody().getNavigationUrls().getSuccess());
//        assertEquals(TestHelpers.CANCEL_URL, actual.getBody().getNavigationUrls().getCancel());
//        assertEquals(TestHelpers.ERROR_URL, actual.getBody().getNavigationUrls().getError());
//    }
//
//    @Test
//    @DisplayName("200: With user not having account details present")
//    public void withUserHavingNoAccountDetailsShouldReturnUserDetailsButNoAccount() {
//
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<GetSubmissionConfigResponse> actual = sut.getSubmissionConfig(
//                TestHelpers.CASE_4, 
//                UUID.randomUUID());
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(TestHelpers.SUCCESS_URL, actual.getBody().getNavigationUrls().getSuccess());
//        assertEquals(TestHelpers.CANCEL_URL, actual.getBody().getNavigationUrls().getCancel());
//        assertEquals(TestHelpers.ERROR_URL, actual.getBody().getNavigationUrls().getError());
//    }
//
//    @Test
//    @DisplayName("403: With user not having universal id claim should throw MissingUniversalIdException")
//    public void withUserNotHavingUniversalIdShouldThrowMissingUniversalIdException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getSubmissionConfig(UUID.randomUUID(), TestHelpers.CASE_5));
//        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
//        Assertions.assertEquals("universal-id claim missing in jwt token.", exception.getMessage());
//    }
//
//    private List<ServiceFees> createFees() {
//        ServiceFees fee1 = new ServiceFees(BigDecimal.TEN, SERVICE_TYPE_CD);
//        ServiceFees fee2 = new ServiceFees(BigDecimal.ONE, SERVICE_TYPE_CD1);
//        return Arrays.asList(fee1, fee2);
//    }

}
