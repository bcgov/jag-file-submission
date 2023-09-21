package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRushProcessingTest {
//
//    private static final String FILE_PDF = "file.pdf";
//    private static final String COUNTRY = "COUNTRY";
//    private static final String COUNTRY_CODE = "CD";
//    private static final String FIRST_NAME = "FIRSTNAME";
//    private static final String LAST_NAME = "LASTNAME";
//    private static final String ORGANIZATION = "ORGANIZATION";
//    private static final String REASON = "REASON";
//    private static final String PHONE_NUMBER = "1231231234";
//    private static final String DATE = "2001-11-26T12:00:00Z";
//    private SubmissionApiDelegateImpl sut;
//
//    @Mock
//    private SubmissionService submissionServiceMock;
//
//    @Mock
//    private SubmissionStore submissionStoreMock;
//
//    @Mock
//    private GenerateUrlResponseMapper generateUrlResponseMapperMock;
//
//    @Mock
//    private NavigationProperties navigationProperties;
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
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
//        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
//        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
//        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);
//
//        SecurityContextHolder.setContext(securityContextMock);
//
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, new RushProcessingMapperImpl());
//    }
//
//    @Test
//    @DisplayName("201")
//    public void withValidParamtersReturnDocumentProperties() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//
//        Submission submission = TestHelpers.buildSubmission();
//
//        Mockito.when(submissionStoreMock.get(Mockito.any())).thenReturn(Optional.of(submission));
//
//        Rush rush = new Rush();
//        rush.setCountry(COUNTRY);
//        rush.setCourtDate(DATE);
//        rush.setCountryCode(COUNTRY_CODE);
//        rush.setFirstName(FIRST_NAME);
//        rush.setLastName(LAST_NAME);
//        rush.setOrganization(ORGANIZATION);
//        rush.setPhoneNumber(PHONE_NUMBER);
//        rush.setReason(REASON);
//        RushDocument document = new RushDocument();
//        document.setFileName(FILE_PDF);
//        rush.setSupportingDocuments(Arrays.asList(document));
//
//        ResponseEntity<Void> actual = sut.postRushProcessing(TestHelpers.CASE_1, UUID.randomUUID(), rush);
//
//        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("404")
//    public void withNoSubmissionReturnNotFound() {
//
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity actual = sut.postRushProcessing(TestHelpers.CASE_2, UUID.randomUUID(), null);
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//    }
//
//
//    @Test
//    @DisplayName("403: with no universal id should throw InvalidUniversalException")
//    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY,null);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.postRushProcessing(TestHelpers.CASE_2, UUID.randomUUID(), null));
//        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
//    }
}
