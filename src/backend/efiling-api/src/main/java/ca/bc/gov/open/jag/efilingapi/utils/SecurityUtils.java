package ca.bc.gov.open.jag.efilingapi.utils;

import ca.bc.gov.open.jag.efilingapi.Keys;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getUniversalIdFromContext() {

        try {
            return Optional.of(((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                            .getKeycloakSecurityContext().getToken().getOtherClaims().get(Keys.UNIVERSAL_ID_CLAIM_KEY).toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String getClientId() {
        try {
            return ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getIssuedFor();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static Optional<String> getApplicationCode() {
        try {
            return Optional.of(((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getOtherClaims().get(Keys.CSO_APPLICATION_CODE).toString());
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
