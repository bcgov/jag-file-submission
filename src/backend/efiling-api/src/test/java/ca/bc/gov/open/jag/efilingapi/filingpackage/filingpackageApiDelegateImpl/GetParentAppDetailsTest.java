package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("FilepackageApiDelegateImplTest")
public class GetParentAppDetailsTest {
//
//    public static final UUID CASE_1 = UUID.randomUUID();
//    public static final UUID CASE_2 = UUID.randomUUID();
//
//    private static final String RETURN_URL = "http://localhost:8080/";
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
//    public void withValidRequestReturnParentAppDetails() {
//
//        ParentAppDetails parentAppDetails = new ParentAppDetails();
//
//        parentAppDetails.setReturnUrl(RETURN_URL);
//        parentAppDetails.setRejectedDocumentFeature(true);
//
//        Mockito.when(filingPackageService.getParentDetails(Mockito.any(), Mockito.any())).thenReturn(Optional.of(parentAppDetails));
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_1);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<ParentAppDetails> result = sut.getParentDetails(BigDecimal.ONE);
//
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//        Assertions.assertEquals(RETURN_URL, result.getBody().getReturnUrl());
//        Assertions.assertTrue(result.getBody().getRejectedDocumentFeature());
//
//    }
//
//    @Test
//    @DisplayName("403: when no universal id should return 403")
//    public void withNoUniversalIdShouldReturn403() {
//
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);
//
//        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getParentDetails(BigDecimal.ONE));
//        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
//
//    }
//
//    @Test
//    @DisplayName("404: when no filling package is found return 404")
//    public void withValidRequestFilingPackageNotFound() {
//
//        Mockito.when(filingPackageService.getParentDetails(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_2);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        FilingPackageNotFoundException exception = Assertions.assertThrows(FilingPackageNotFoundException.class, () -> sut.getParentDetails(BigDecimal.TEN));
//        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(), exception.getErrorCode());
//
//    }

}
