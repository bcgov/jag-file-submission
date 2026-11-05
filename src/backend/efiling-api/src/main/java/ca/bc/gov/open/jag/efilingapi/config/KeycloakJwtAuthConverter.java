package ca.bc.gov.open.jag.efilingapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Converter implementation (specific to Keycloak) that can convert a Keycloak
 * JWT into a AbstractAuthenticationToken that can be used by Spring Security.
 */
@Component
public class KeycloakJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	public static final String KEYCLOAK_PRINCIPLE_ATTRIBUTE = "preferred_username";
	public static final String KEYCLOAK_RESOURCE_ATTRIBUTE = "resource_access";
	public static final String KEYCLOAK_ROLE_ATTRIBUTE = "roles";

	private String resourceId;
	
	public KeycloakJwtAuthConverter(@Value("${jwt.auth.converter.resource-id}") String resourceId) {
		this.resourceId = resourceId;		
	}

	@Override
	public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

		Collection<GrantedAuthority> authorities = Stream
				.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
				.collect(Collectors.toSet());

		return new JwtAuthenticationToken(jwt, authorities, getPrincipleClaimName(jwt));
	}

	/**
	 * Extract the principle claim name (preferred_username) from the JWT.
	 */
	public static String getPrincipleClaimName(Jwt jwt) {
		return jwt.getClaim(KEYCLOAK_PRINCIPLE_ATTRIBUTE);
	}

	/**
	 * Extracts the resource roles from the JWT.
	 */
	@SuppressWarnings("unchecked")
	private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
		Map<String, Object> resourceAccess = jwt.getClaim(KEYCLOAK_RESOURCE_ATTRIBUTE);
		if (resourceAccess == null) {
			return Set.of();
		}

		Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);
		if (resource == null) {
			return Set.of();
		}

		Collection<String> resourceRoles = (Collection<String>) resource.get(KEYCLOAK_ROLE_ATTRIBUTE);
		if (resourceRoles == null) {
			return Set.of();
		}
		
		return resourceRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toSet());
	}

}
