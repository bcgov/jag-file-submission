package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

import org.junit.jupiter.api.TestInstance;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteSubmissionTest {
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
//    private DocumentStore documentStoreMock;
//
//    @Mock
//    private AccountService accountServiceMock;
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
//
//    @BeforeEach
//    public void setUp() {
//
//        MockitoAnnotations.initMocks(this);
//
//        Mockito.doNothing().when(documentStoreMock).evict(Mockito.any(), Mockito.any());
//        Mockito.doNothing().when(submissionStoreMock).evict(Mockito.any());
//
//        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
//        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
//        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
//        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);
//
//        SecurityContextHolder.setContext(securityContextMock);
//
//        Submission submission = Submission
//                .builder()
//                .universalId(UUID.randomUUID().toString())
//                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), createDocumentList(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
//                .create();
//
//        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submission));
//
//
//        NavigationProperties navigationProperties = new NavigationProperties();
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, new GenerateUrlResponseMapperImpl(), navigationProperties, submissionStoreMock, documentStoreMock, null, filingPackageMapper, generateUrlRequestValidator, null);
//
//    }
//
//    @Test
//    @DisplayName("200: should delete from submission")
//    public void withSubmissionIdAndTransactionIdShouldDeleteSubmission() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity actual = sut.deleteSubmission(TestHelpers.CASE_1, UUID.randomUUID());
//
//        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("404: without submission should return not found")
//    public void withoutSubmissionShouldDeleteSubmission() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity actual = sut.deleteSubmission(TestHelpers.CASE_2, UUID.randomUUID());
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("403: with no universal id should throw InvalidUniversalException")
//    public void withUserNotHavingUniversalIdShouldThrowInvalidUniversalException() {
//
//
//        BigDecimal test = new BigDecimal(100000000);
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY,null);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.deleteSubmission(TestHelpers.CASE_2, UUID.randomUUID()));
//        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
//
//    }


}
