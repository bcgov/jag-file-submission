package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("FilepackageApiDelegateImplTest")
public class GetRushDocumentTest {
//    public static final UUID CASE_1 = UUID.randomUUID();
//    public static final UUID CASE_2 = UUID.randomUUID();
//
//    public static  final String FOUND_DOCUMENT_IDENTIFIER = "1";
//
//    public static  final String NOT_FOUND_DOCUMENT_IDENTIFIER = "10";
//
//    public static final byte[] BYTES = "TEST".getBytes();
//
//    FilingpackageApiDelegateImpl sut;
//
//    @Mock
//    FilingPackageService filingPackageService;
//
//    @Mock
//    private SecurityContext securityContextMock;
//
//    @Mock
//    private Authentication authenticationMock;
//
//    @Mock
//    private KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipalMock;
//
//    @Mock
//    private KeycloakSecurityContext keycloakSecurityContextMock;
//
//    @Mock
//    private AccessToken tokenMock;
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
//        Mockito.when(filingPackageService.getRushDocument(Mockito.any(), ArgumentMatchers.eq(BigDecimal.ONE), Mockito.any())).thenReturn(Optional.of(SubmittedDocument.builder()
//                .data(new ByteArrayResource(BYTES))
//                .name("TEST")
//                .create()));
//
//
//        Mockito.when(filingPackageService.getRushDocument(Mockito.any(), ArgumentMatchers.eq(BigDecimal.TEN), Mockito.any())).thenReturn(Optional.empty());
//
//        sut = new FilingpackageApiDelegateImpl(filingPackageService);
//    }
//
//    @Test
//    @DisplayName("200: File is returned")
//    public void withValidRequestReturnFilingPackage() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_1);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<Resource> result = sut.getRushDocument(BigDecimal.ONE, FOUND_DOCUMENT_IDENTIFIER);
//
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("403: when no universal id should return 403")
//    public void withNoUniversalIdShouldReturn403() {
//
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);
//
//        ResponseEntity<?> actual = sut.getRushDocument(BigDecimal.ONE, NOT_FOUND_DOCUMENT_IDENTIFIER);
//
//        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("404: when no document should return 404")
//    public void withNoDocumentShouldReturn404() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_1);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<?> actual = sut.getRushDocument(BigDecimal.TEN, NOT_FOUND_DOCUMENT_IDENTIFIER);
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//
//    }

}
