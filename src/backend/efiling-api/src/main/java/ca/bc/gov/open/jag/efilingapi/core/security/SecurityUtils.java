package ca.bc.gov.open.jag.efilingapi.core.security;

import java.util.Optional;

import ca.bc.gov.open.jag.efilingapi.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getClientId() {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return Optional.of(jwt.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getUniversalIdFromContext() {
        return getOtherClaim(Keys.UNIVERSAL_ID_CLAIM_KEY);
    }

    public static Optional<String> getApplicationCode() {
        return getOtherClaim(Keys.CSO_APPLICATION_CLAIM_KEY);
    }

    public static Optional<String> getIdentityProvider() {
        return getOtherClaim(Keys.IDENTITY_PROVIDER_CLAIM_KEY);
    }

    private static Optional<String> getOtherClaim(String claim) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.of(jwt.getClaim(claim));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean isEarlyAdopter() {
        return isInRole("ROLE_early-adopters");
    }

    public static boolean isInRole(String role) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            return authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals(role));
        } catch (Exception e) {
            return false;
        }
    }
}
