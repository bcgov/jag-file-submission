package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

// FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("FilepackageApiDelegateImplTest")
public class DeleteSubmittedDocumentTest {
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
//        Mockito.doNothing().when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.ONE), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_ONE));
//
//        Mockito.doThrow(RuntimeException.class).when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.ONE), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO));
//
//        Mockito.doThrow(EfilingAccountServiceException.class).when(filingPackageService).deleteSubmittedDocument(any(), ArgumentMatchers.eq(BigDecimal.TEN), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO));
//
//        sut = new FilingpackageApiDelegateImpl(filingPackageService);
//    }
//
//    @Test
//    @DisplayName("200: ok document was deleted")
//    public void withValidRequestReturnFilingPackage() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<?> result = sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_ONE);
//
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("403: when no universal id should throw MissingUniversalIdException")
//    public void withNoUniversalIdShouldThrowMissingUniversalIdException() {
//
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);
//
//        MissingUniversalIdException exception = Assertions.assertThrows(MissingUniversalIdException.class, () -> sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_ONE));
//        Assertions.assertEquals(ErrorCode.MISSING_UNIVERSAL_ID.toString(), exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("400: when delete failed should throw DeleteDocumentException")
//    public void withDeleteFailedShouldThrowDeleteDocumentException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        DeleteDocumentException exception = Assertions.assertThrows(DeleteDocumentException.class, () -> sut.deleteSubmittedDocument(BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO));
//        Assertions.assertEquals(ErrorCode.DELETE_DOCUMENT_ERROR.toString(), exception.getErrorCode());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
//    }
//
//    @Test
//    @DisplayName("404: when delete failed should throw DeleteDocumentException")
//    public void withDeleteAccountNotFoundFailedShouldThrowDeleteDocumentException() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, TestHelpers.CASE_1_STRING);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        DeleteDocumentException exception = Assertions.assertThrows(DeleteDocumentException.class, () -> sut.deleteSubmittedDocument(BigDecimal.TEN, TestHelpers.DOCUMENT_ID_TWO));
//        Assertions.assertEquals(ErrorCode.DELETE_DOCUMENT_ERROR.toString(), exception.getErrorCode());
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//    }

}
