package ca.bc.gov.open.jag.efilingapi.submission.submissionApiDelegateImpl;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DisplayName("SubmissionApiDelegateImpl test suite")
public class GetPackageInformationTest {
//
//    private SubmissionApiDelegateImpl sut;
//
//    @Mock
//    private SubmissionService submissionServiceMock;
//
//    @Mock
//    private GenerateUrlResponseMapper generateUrlResponseMapperMock;
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
//        NavigationProperties navigationProperties = new NavigationProperties();
//        navigationProperties.setBaseUrl("http://localhost");
//
//        Submission submissionWithParentApplication = Submission
//                .builder()
//                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), createDocumentListWithNulls(), TestHelpers.createPartyList(), TestHelpers.createOrganizationList()))
//                .create();
//
//        Mockito.when(submissionStoreMock.get(ArgumentMatchers.argThat(x -> x.getSubmissionId().equals(TestHelpers.CASE_1)))).thenReturn(Optional.of(submissionWithParentApplication));
//
//        Mockito.when(submissionServiceMock.isRushRequired(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
//
//        FilingPackageMapper filingPackageMapper = new FilingPackageMapperImpl();
//        sut = new SubmissionApiDelegateImpl(submissionServiceMock, accountServiceMock, generateUrlResponseMapperMock, navigationProperties, submissionStoreMock, documentStoreMock, clamAvServiceMock, filingPackageMapper, generateUrlRequestValidator, null);
//    }
//
//    @Test
//    @DisplayName("200: pass id and get values")
//    public void withCorrectIDReturnResult() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<SubmissionFilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_1);
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        Assertions.assertEquals(TestHelpers.DIVISION, actual.getBody().getCourt().getDivision());
//        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getBody().getCourt().getFileNumber());
//        Assertions.assertEquals(TestHelpers.LEVEL, actual.getBody().getCourt().getLevel());
//        Assertions.assertEquals(TestHelpers.LOCATION, actual.getBody().getCourt().getLocation());
//        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getBody().getCourt().getParticipatingClass());
//        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getBody().getCourt().getCourtClass());
//        Assertions.assertEquals(TestHelpers.TYPE, actual.getBody().getDocuments().get(0).getType());
//        Assertions.assertTrue(actual.getBody().getDocuments().get(0).getRushRequired());
//        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getDocuments().get(0).getDescription());
//        Assertions.assertNull(actual.getBody().getDocuments().get(0).getIsAmendment());
//        Assertions.assertNull(actual.getBody().getDocuments().get(0).getIsSupremeCourtScheduling());
//
//    }
//
//    @Test
//    @DisplayName("404: with incorrect id return 404")
//    public void withInCorrectIDReturnNotFound() {
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<SubmissionFilingPackage> actual = sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2);
//        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("404: with no universal id should throw InvalidUniversalException")
//    public void withInCorrectIDThrowInvalidUniversalException() {
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, null);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        InvalidUniversalException exception = Assertions.assertThrows(InvalidUniversalException.class, () -> sut.getSubmissionFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2));
//        Assertions.assertEquals(ErrorCode.INVALIDUNIVERSAL.toString(), exception.getErrorCode());
//    }
//
//    private List<Document> createDocumentListWithNulls() {
//        return Arrays.asList(Document.builder()
//                .description(TestHelpers.DESCRIPTION)
//                .statutoryFeeAmount(BigDecimal.TEN)
//                .name("random.txt")
//                .type(TestHelpers.TYPE)
//                .subType(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD)
//                .mimeType("application/txt")
//                .isSupremeCourtScheduling(null)
//                .isAmendment(null)
//                .data(new Object()).create());
//    }
}
