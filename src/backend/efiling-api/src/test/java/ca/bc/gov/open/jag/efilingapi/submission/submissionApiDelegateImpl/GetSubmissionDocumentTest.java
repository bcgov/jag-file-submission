package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetSubmissionDocumentTest {
//
//    public static final String CONTENT = "content";
//    private SubmissionApiDelegateImpl sut;
//
//    @Mock
//    private SubmissionService submissionServiceMock;
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
//    @BeforeAll
//    public void setUp() {
//
//        MockitoAnnotations.initMocks(this);
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
//
//        Mockito.when(documentStoreMock.get(Mockito.any(), Mockito.endsWith("test.txt"))).thenReturn(CONTENT.getBytes());
//
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
//
//    }
//
//
//    @Test
//    @DisplayName("200: when document in cache should return 200")
//    public void withDocumentInCacheShouldReturn200() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<Resource> actual = sut.getSubmissionDocument(UUID.randomUUID(), UUID.randomUUID(), "test.txt");
//
//        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("404: when document is missing should return 404")
//    public void withNoDocumentShouldReturn404() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<Resource> actual = sut.getSubmissionDocument(UUID.randomUUID(), UUID.randomUUID(), "test2.txt");
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("403: without universal id should throw InvalidUniversalException")
//    public void withNoUniversalIdShouldThrowInvalidUniversalException() {
//
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);
//
//        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.getSubmissionDocument(UUID.randomUUID(), UUID.randomUUID(), "test2.txt"));
//        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
//    }
}
