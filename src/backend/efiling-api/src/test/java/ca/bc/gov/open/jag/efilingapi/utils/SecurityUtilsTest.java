package ca.bc.gov.open.jag.efilingapi.utils;

import ca.bc.gov.open.jag.efilingapi.Keys;
import ca.bc.gov.open.jag.efilingapi.core.security.SecurityUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.open.jag.efilingapi.Keys.UNIVERSAL_ID_CLAIM_KEY;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Security Utils Test Suite")
public class SecurityUtilsTest {


    private static final String EXPECTED_CLAIM = "claim_value";

    @Mock
    private SecurityContext securityContextMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private Jwt jwtMock;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    public void shouldConvertToUUID() {

        String expectedUUID = UUID.randomUUID().toString();

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(expectedUUID);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        Optional<String> actual = SecurityUtils.getUniversalIdFromContext();

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expectedUUID, actual.get());

    }

    @Test
    @DisplayName("client Id should return value")
    public void withClientIdShouldReturnTrue() {

        String username = "username";

        Mockito.when(jwtMock.getId()).thenReturn(username);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        Optional<String> actual =  SecurityUtils.getClientId();
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(username, actual.get());

    }

    @Test
    @DisplayName("No client Id should return empty")
    public void withNoClientIdShouldReturnFalse() {

        Mockito.when(securityContextMock.getAuthentication()).thenThrow(new RuntimeException());
        Optional<String> actual =  SecurityUtils.getClientId();
        Assertions.assertFalse(actual.isPresent());

    }

    @Test
    @DisplayName("Should return universal id")
    public void shouldReturnClaim() {

        Mockito.when(jwtMock.getClaim(Mockito.eq(UNIVERSAL_ID_CLAIM_KEY))).thenReturn(EXPECTED_CLAIM);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(jwtMock);

        Optional<String> actual = SecurityUtils.getUniversalIdFromContext();

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(EXPECTED_CLAIM, actual.get());

    }

    @Test
    @DisplayName("null should throw")
    public void exceptionIsInRoleThrows() {

        Assertions.assertFalse(SecurityUtils.isInRole(""));

    }

}
