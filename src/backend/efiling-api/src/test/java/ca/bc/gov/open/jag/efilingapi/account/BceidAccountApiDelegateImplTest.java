package ca.bc.gov.open.jag.efilingapi.account;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BceidAccountApiDelegateImplTest {
//
//    public static final UUID CASE_1 = UUID.randomUUID();
//    public static final UUID CASE_2 = UUID.randomUUID();
//
//    private BceidAccountApiDelagateImpl sut;
//
//    @Mock
//    private BCeIDAccountService bCeIDAccountServiceMock;
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
//    public void setup() {
//
//        Optional<IndividualIdentity> identity = Optional.of(new IndividualIdentity(new Name(
//                "firstName",
//                "middleName",
//                "otherMiddleName",
//                "surname",
//                "initials"
//                ), null, null, null));
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
//        Mockito
//                .doReturn(identity)
//                .when(bCeIDAccountServiceMock)
//                .getIndividualIdentity(ArgumentMatchers.argThat(x -> x.getId().equals(CASE_1.toString().replace("-", "").toUpperCase())));
//
//        Mockito
//                .doReturn(Optional.empty())
//                .when(bCeIDAccountServiceMock)
//                .getIndividualIdentity(ArgumentMatchers.argThat(x -> x.getId().equals(CASE_2.toString().replace("-", "").toUpperCase())));
//
//        BceidAccountMapper bceidAccountMapper = new BceidAccountMapperImpl();
//        sut = new BceidAccountApiDelagateImpl(bCeIDAccountServiceMock, bceidAccountMapper);
//
//    }
//
//    @Test
//    @DisplayName("200: should return bceid account")
//    public void withAccountShouldReturnAccount() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_1);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity<BceidAccount> actual = sut.getBceidAccount(UUID.randomUUID());
//
//        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
//        Assertions.assertEquals("firstName", actual.getBody().getFirstName());
//        Assertions.assertEquals("middleName", actual.getBody().getMiddleName());
//        Assertions.assertEquals("surname", actual.getBody().getLastName());
//
//    }
//
//    @Test
//    @DisplayName("403: when no universal id should return 403")
//    public void withNoUniversalIdShouldReturn403() {
//
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);
//
//        ResponseEntity actual = sut.getBceidAccount(UUID.randomUUID());
//
//        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
//
//    }
//
//    @Test
//    @DisplayName("404: when user not found should return 404")
//    public void withNoUserShouldReturn404() {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_2);
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//        ResponseEntity actual = sut.getBceidAccount(UUID.randomUUID());
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//
//    }

}
