package ca.bc.gov.open.jag.efilingapi.utils;

import ca.bc.gov.open.jag.efilingapi.Keys;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<UUID> getUniversalIdFromContext() {

        try {
            return stringToUUID(((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
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

    public static String getApplicationCode() {
        try {
            return ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getKeycloakSecurityContext().getToken().getOtherClaims().get(Keys.CSO_APPLICATION_CODE).toString();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static Optional<UUID> stringToUUID(String id) {
        try {
            return Optional.of(UUID.fromString(id.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            )));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean isBetaUser() {
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
