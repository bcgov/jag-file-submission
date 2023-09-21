package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("FilePackageServiceImplTest")
public class GetFilingPackagesTest {
//
//    public static final String PARENT_APPLICATION_FOUND = "FOUND";
//    public static final String PARENT_APPLICATION_NOT_FOUND = "NOT_FOUND";
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
//        Mockito.when(filingPackageService.getFilingPackages(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING), ArgumentMatchers.eq(PARENT_APPLICATION_FOUND))).thenReturn(Optional.of(new ArrayList<>()));
//
//        Mockito.when(filingPackageService.getFilingPackages(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING), ArgumentMatchers.eq(PARENT_APPLICATION_FOUND))).thenReturn(Optional.empty());
//
//        sut = new FilingpackageApiDelegateImpl(filingPackageService);
//    }
//
//    @Test
//    @DisplayName("200: ok filingpackages returned")
//    public void withValidRequestReturnFilingPackages() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<List<FilingPackage>> actual = sut.getFilingPackages(PARENT_APPLICATION_FOUND);
//
//        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("403: when no universal id should throw MissingUniversalIdException")
//    public void withNoUniversalIdShouldThrowMissingUniversalIdException() {
//
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);
//
//        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.getFilingPackages(PARENT_APPLICATION_FOUND));
//        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("404: when no filling packages found should throw FilingPackageNotFoundException")
//    public void withValidRequestNotFoundShouldThrowFilingPackageNotFoundException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_2_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        FilingPackageNotFoundException exception = Assertions.assertThrows(FilingPackageNotFoundException.class, () -> sut.getFilingPackages(PARENT_APPLICATION_NOT_FOUND));
//        Assertions.assertEquals(ErrorCode.FILING_PACKAGE_NOT_FOUND.toString(), exception.getErrorCode());
//    }


}
