package ca.bc.gov.open.jag.efilingapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

	private final KeycloakJwtAuthConverter keycloakJwtAuthConverter;

	public SecurityConfig(KeycloakJwtAuthConverter keycloakJwtAuthConverter) {
		this.keycloakJwtAuthConverter = keycloakJwtAuthConverter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		http.csrf(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests(requests -> requests
						.requestMatchers(antMatcher("/actuator/**")).permitAll()
						.anyRequest().authenticated());

		http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthConverter)));

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	protected CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.applyPermitDefaultValues();
		corsConfiguration.addAllowedMethod(HttpMethod.GET);
		corsConfiguration.addAllowedMethod(HttpMethod.POST);
		corsConfiguration.addAllowedMethod(HttpMethod.PUT);
		corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
		corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

}
