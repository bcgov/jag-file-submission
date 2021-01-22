package ca.bc.gov.open.jag.efilingapi.utils;

import ca.bc.gov.open.jag.efilingapi.Keys;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getClientId() {
        try {
            return ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getIssuedFor();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static Optional<String> getOtherClaim(String claim) {
        try {
            return Optional.of(((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getOtherClaims().get(claim).toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public static boolean isEarlyAdopter() {
        return isInRole("early-adopters");
    }

    public static boolean isInRole(String role) {
        try {
            return ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getResourceAccess(Keys.EFILING_API_NAME).isUserInRole(role);
        } catch (Exception e) {
            return false;
        }
    }
}
