package ca.bc.gov.open.jag.efilingapi.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class KeycloakJwtAuthConverterTest {

	@Test
	void testNull() {
		KeycloakJwtAuthConverter converter = new KeycloakJwtAuthConverter(null);
		assertThrows(NullPointerException.class, () -> converter.convert(null));
	}

	@Test
	void testJwtMissingResourceAccess() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);
		
		KeycloakJwtAuthConverter converter = new KeycloakJwtAuthConverter(null);
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(0, authorities.size());
	}

	@Test
	void testJwtMissingResource() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);
		Map<String, Object> resourceAccess = new HashMap<String, Object>();
		when(jwt.getClaim(KeycloakJwtAuthConverter.KEYCLOAK_RESOURCE_ATTRIBUTE)).thenReturn(resourceAccess);
		
		KeycloakJwtAuthConverter converter = new KeycloakJwtAuthConverter(null);
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(0, authorities.size());
	}

	@Test
	void testJwtMissingRoles() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);
		
		Map<String, Object> resource = new HashMap<String, Object>();
		
		Map<String, Object> resourceAccess = new HashMap<String, Object>();
		resourceAccess.put("efiling-hub", resource);
		when(jwt.getClaim(KeycloakJwtAuthConverter.KEYCLOAK_RESOURCE_ATTRIBUTE)).thenReturn(resourceAccess);
		
		KeycloakJwtAuthConverter converter = new KeycloakJwtAuthConverter("efiling-hub");
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(0, authorities.size());
	}

	@Test
	void testJwtValid() throws Exception {
		Jwt jwt = Mockito.mock(Jwt.class);
		
		Collection<String> resourceRoles = new ArrayList<>();
		resourceRoles.add("tester");
		
		Map<String, Object> resource = new HashMap<String, Object>();
		resource.put(KeycloakJwtAuthConverter.KEYCLOAK_ROLE_ATTRIBUTE, resourceRoles);
		
		Map<String, Object> resourceAccess = new HashMap<String, Object>();
		resourceAccess.put("efiling-hub", resource);
		when(jwt.getClaim(KeycloakJwtAuthConverter.KEYCLOAK_RESOURCE_ATTRIBUTE)).thenReturn(resourceAccess);
		
		KeycloakJwtAuthConverter converter = new KeycloakJwtAuthConverter("efiling-hub");
		AbstractAuthenticationToken authenticationToken = converter.convert(jwt);
		Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();
		assertEquals(1, authorities.size());
		assertEquals("ROLE_tester", authorities.iterator().next().getAuthority());
	}
}
