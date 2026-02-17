package ca.bc.gov.open.jag.efilingapi.utils;

import org.apache.commons.lang3.StringUtils;

public class UniversalIdUtils {

    private UniversalIdUtils() {}

    public static String sanitizeUniversalId(String universalId) {
        return StringUtils.replace(universalId, "-", "").toUpperCase();
    }

}
