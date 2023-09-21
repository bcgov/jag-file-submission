package ca.bc.gov.open.jag.efilingapi.core.mdc;

//FIXME: Clean up and replace calls to Keycloak with OAuth2 security instead
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MdcFilterTest {
//
//    private MdcFilter sut;
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
//
//        sut = new MdcFilter();
//
//
//
//    }
//
//    @Test
//    @DisplayName("test that the MDCFilter does not throw exception while running with lambda request")
//    public void globalTestFilter() throws IOException, ServletException {
//
//        Map<String, Object> otherClaims = new HashMap<>();
//        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, UUID.randomUUID());
//        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);
//
//
//        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET", "/efilinghub/submission");
//        mockHttpServletRequest.addHeader("authorization", "token");
//        mockHttpServletRequest.addHeader("random", "random");
//
//        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
//        MockFilterChain mockFilterChain = new MockFilterChain();
//
//        sut.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
//
//
//
//    }
//



}
