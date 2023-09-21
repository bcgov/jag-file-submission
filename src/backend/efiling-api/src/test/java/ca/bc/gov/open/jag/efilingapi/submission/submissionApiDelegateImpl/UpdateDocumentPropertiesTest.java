package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("Upload Additional Submission Documents Test Suite")
public class UpdateDocumentPropertiesTest {
//
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
//    public void setUp() throws IOException {
//        MockitoAnnotations.initMocks(this);
//
//        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
//        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
//        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
//        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);
//
//        SecurityContextHolder.setContext(securityContextMock);
//
//        Submission submission = TestHelpers.buildSubmission();
//
//        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));
//
//
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
//    }
//
//    @Test
//    @DisplayName("200")
//    public void withValidParamtersReturnDocumentProperties() {
//
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
//        DocumentProperties documentProperties = new DocumentProperties();
//        documentProperties.setType("AAB");
//        documentProperties.setName("test.txt");
//        documentProperties.setIsAmendment(true);
//        documentProperties.setIsSupremeCourtScheduling(true);
//        updateDocumentRequest.addDocumentsItem(documentProperties);
//
//        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(updateDocumentRequest), Mockito.any())).thenReturn(Submission
//                .builder()
//                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
//                .navigationUrls(TestHelpers.createNavigation(TestHelpers.SUCCESS_URL, TestHelpers.CANCEL_URL, TestHelpers.ERROR_URL))
//                .create());
//
//        ResponseEntity<UpdateDocumentResponse> actual = sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), updateDocumentRequest);
//
//        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
//        Assertions.assertEquals(1, actual.getBody().getDocuments().size());
//        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getDocuments().get(0).getDescription());
//        Assertions.assertEquals(BigDecimal.TEN, actual.getBody().getDocuments().get(0).getStatutoryFeeAmount());
//        Assertions.assertEquals(true, actual.getBody().getDocuments().get(0).getIsSupremeCourtScheduling());
//        Assertions.assertEquals(true, actual.getBody().getDocuments().get(0).getIsAmendment());
//    }
//
//    @Test
//    @DisplayName("400: with invalid parameters should throw DocumentRequiredException")
//    public void withInValidParamtersShouldThrowDocumentRequiredException() {
//
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        DocumentRequiredException exception = Assertions.assertThrows(DocumentRequiredException.class, () -> sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), null));
//        Assertions.assertEquals(ErrorCode.DOCUMENT_REQUIRED.toString(), exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("500: with exception thrown from Soap should throw DocumentTypeException")
//    public void withExceptionThrownFromSoapShouldThrowDocumentTypeException() {
//
//
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        UpdateDocumentRequest errorDocumentRequest = new UpdateDocumentRequest();
//        errorDocumentRequest.addDocumentsItem(new DocumentProperties());
//
//        Mockito.when(submissionServiceMock.updateDocuments(any(), Mockito.refEq(errorDocumentRequest), Mockito.any())).thenThrow(new EfilingDocumentServiceException("NOOOOOOO"));
//
//        DocumentTypeException exception = Assertions.assertThrows(DocumentTypeException.class, () -> sut.updateDocumentProperties(TestHelpers.CASE_1, UUID.randomUUID(), errorDocumentRequest));
//        Assertions.assertEquals(ErrorCode.DOCUMENT_TYPE_ERROR.toString(), exception.getErrorCode());
//    }
//
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
//
//        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
//        updateDocumentRequest.addDocumentsItem(new DocumentProperties());
//        ResponseEntity actual = sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest);
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
//        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
//        updateDocumentRequest.addDocumentsItem(new DocumentProperties());
//
//        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.updateDocumentProperties(TestHelpers.CASE_2, UUID.randomUUID(), updateDocumentRequest));
//        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
//    }
}
