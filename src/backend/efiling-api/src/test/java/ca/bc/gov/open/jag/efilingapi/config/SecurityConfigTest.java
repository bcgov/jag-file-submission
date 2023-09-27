package ca.bc.gov.open.jag.efilingapi.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class SecurityConfigTest {

	@Mock
	KeycloakJwtAuthConverter jwtAuthConverter;

	@Mock
	HttpSecurity http;

	//@Test
	void testFilterChain() throws Exception {
		MockitoAnnotations.openMocks(this);
		when(http.build()).thenReturn(Mockito.mock(DefaultSecurityFilterChain.class));
		SecurityConfig config = new SecurityConfig(jwtAuthConverter);
		assertNotNull(config.filterChain(http));
		assertNotNull(config.corsConfigurationSource());
	}

	//@Test
	void testSessionAuthenticationStrategy() throws Exception {
		KeycloakJwtAuthConverter jwtAuthConverter = Mockito.mock(KeycloakJwtAuthConverter.class);
		//HttpSecurity http = Mockito.mock(HttpSecurity.class);

		SecurityConfig config = new SecurityConfig(jwtAuthConverter);
		assertNotNull(config.sessionAuthenticationStrategy());
	}

}
