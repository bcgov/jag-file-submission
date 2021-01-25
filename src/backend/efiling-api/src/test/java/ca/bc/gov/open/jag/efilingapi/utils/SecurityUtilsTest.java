package ca.bc.gov.open.jag.efilingapi.utils;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import org.junit.jupiter.api.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Security Utils Test Suite")
public class SecurityUtilsTest {


    private static final String EXPECTED_CLAIM = "claim_value";

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private KeycloakPrincipal keycloakPrincipalMock;

    @Mock
    private KeycloakSecurityContext keycloakSecurityContextMock;

    @Mock
    private AccessToken tokenMock;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);
        Mockito.when(keycloakPrincipalMock.getKeycloakSecurityContext()).thenReturn(keycloakSecurityContextMock);
        Mockito.when(keycloakSecurityContextMock.getToken()).thenReturn(tokenMock);

        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    public void shouldConvertToUUID() {

        String expectedUUID = UUID.randomUUID().toString();

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, expectedUUID);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);


        Optional<String> actual = SecurityUtils.getUniversalIdFromContext();

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expectedUUID, actual.get());

    }

    @Test
    @DisplayName("No client Id should return empty")
    public void withNoClientIdShouldReturnFalse() {

        Mockito.when(securityContextMock.getAuthentication()).thenThrow(new RuntimeException());
        Optional<String> actual =  SecurityUtils.getClientId();
        Assertions.assertFalse(actual.isPresent());

    }

    @Test
    public void shouldReturnClaim() {

        Map<String, Object> otherClaims = new HashMap<>();
        otherClaims.put(Keys.UNIVERSAL_ID_CLAIM_KEY, EXPECTED_CLAIM);
        Mockito.when(tokenMock.getOtherClaims()).thenReturn(otherClaims);


        Optional<String> actual = SecurityUtils.getUniversalIdFromContext();

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EXPECTED_CLAIM, actual.get());

    }


}
