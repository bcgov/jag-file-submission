package ca.bc.gov.open.jag.efilingapi.core.security;

import java.util.Optional;

import ca.bc.gov.open.jag.efilingapi.Keys;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getClientId() {
        try {
            return Optional.empty();
            // FIXME: replace this expression to Keycloak with OAuth2 security
//            return Optional.of(((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                    .getKeycloakSecurityContext().getToken().getIssuedFor());
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
        	// FIXME: replace this expression to Keycloak with OAuth2 security
//            return Optional.of(((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                    .getKeycloakSecurityContext().getToken().getOtherClaims().get(claim).toString());
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean isEarlyAdopter() {
        return isInRole("early-adopters");
    }

    public static boolean isInRole(String role) {
        try {
        	// FIXME: replace this expression to Keycloak with OAuth2 security
//            return ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                    .getKeycloakSecurityContext().getToken().getResourceAccess(Keys.EFILING_API_NAME).isUserInRole(role);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
