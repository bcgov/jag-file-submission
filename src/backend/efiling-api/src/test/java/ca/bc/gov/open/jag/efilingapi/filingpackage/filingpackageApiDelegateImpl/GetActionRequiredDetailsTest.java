package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("FilepackageApiDelegateImplTest")
public class GetActionRequiredDetailsTest {
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
//    private KeycloakPrincipal keycloakPrincipalMock;
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
//        Mockito.when(filingPackageService.getActionRequiredDetails(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING), ArgumentMatchers.eq(BigDecimal.ONE))).thenReturn(Optional.of(new ActionRequiredDetails()));
//
//
//        Mockito.when(filingPackageService.getActionRequiredDetails(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING), ArgumentMatchers.eq(BigDecimal.TEN))).thenReturn(Optional.empty());
//
//        sut = new FilingpackageApiDelegateImpl(filingPackageService);
//    }
//
//    @Test
//    @DisplayName("200: ok action required details returned")
//    public void withValidRequestReturnActionRequiredDetails() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<ActionRequiredDetails> result = sut.getActionRequiredDetails(BigDecimal.ONE);
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
//        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getActionRequiredDetails(BigDecimal.ONE));
//        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
//
//    }
//
//    @Test
//    @DisplayName("404: when no filling package is found should throw FilingPackageNotFoundException")
//    public void withValidRequestNotFoundShouldThrowFilingPackageNotFoundException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_2_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        FilingPackageNotFoundException exception = Assertions.assertThrows(FilingPackageNotFoundException.class, () -> sut.getActionRequiredDetails(BigDecimal.ONE));
//        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(), exception.getErrorCode());
//
//    }

}

