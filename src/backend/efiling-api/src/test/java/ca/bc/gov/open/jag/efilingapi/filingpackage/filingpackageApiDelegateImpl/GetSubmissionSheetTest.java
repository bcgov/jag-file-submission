package ca.bc.gov.open.jag.efilingapi.filingpackage.filingpackageApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.filingpackage.FilingpackageApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilepackageApiDelegateImplTest")
public class GetSubmissionSheetTest {
    public static final UUID CASE_1 = UUID.randomUUID();
    public static final UUID CASE_2 = UUID.randomUUID();

    public static final byte[] BYTES = "TEST".getBytes();

    FilingpackageApiDelegateImpl sut;

    @Mock
    FilingPackageService filingPackageService;

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);

        Mockito.when(filingPackageService.getSubmissionSheet(ArgumentMatchers.eq(BigDecimal.ONE))).thenReturn(Optional.of(new ByteArrayResource(BYTES)));


        Mockito.when(filingPackageService.getSubmissionSheet(ArgumentMatchers.eq(BigDecimal.TEN))).thenReturn(Optional.empty());

        sut = new FilingpackageApiDelegateImpl(filingPackageService);
    }

    @Test
    @DisplayName("200: ok url was generated")
    public void withValidRequestReturnFilingPackage() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_1);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<Resource> result = sut.getSubmissionSheet(BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    @DisplayName("403: when no universal id should return 403")
    public void withNoUniversalIdShouldReturn403() {

        Mockito.when(tokenMock.getOtherClaims()).thenReturn(null);

        ResponseEntity<?> actual = sut.getSubmissionSheet(BigDecimal.ONE);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: when no filling package is found return 404")
    public void withValidRequestFilingPackageNotFound() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, CASE_2);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);

        ResponseEntity<Resource> result = sut.getSubmissionSheet(BigDecimal.TEN);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }
}
