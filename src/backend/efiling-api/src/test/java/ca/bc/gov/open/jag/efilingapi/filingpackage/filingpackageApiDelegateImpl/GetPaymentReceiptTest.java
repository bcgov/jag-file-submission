package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("GetPaymentReceiptTest")
public class GetPaymentReceiptTest {
//    public static final UUID CASE_1 = UUID.randomUUID();
//    public static final UUID CASE_2 = UUID.randomUUID();
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
//        sut = new FilingpackageApiDelegateImpl(filingPackageService);
//    }
//
//    @Test
//    @DisplayName("200: ok url was generated")
//    public void withValidRequestReturnFilingPackage() {
//
//        Mockito.when(filingPackageService.getReport(Mockito.any())).thenReturn(Optional.of(new ByteArrayResource(BYTES)));
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_1);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<Resource> result = sut.getPaymentReceipt(BigDecimal.ONE);
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
//        ResponseEntity<?> actual = sut.getPaymentReceipt(BigDecimal.ONE);
//
//        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("404: when no filling package is found return 404")
//    public void withValidRequestFilingPackageNotFound() {
//
//        Mockito.when(filingPackageService.getReport(Mockito.any())).thenReturn(Optional.empty());
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_2);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<Resource> result = sut.getPaymentReceipt(BigDecimal.TEN);
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//
//    }

}
