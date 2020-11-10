package ca.bc.gov.open.jag.efilingcommons.utils;

import org.springframework.util.StringUtils;

public class BooleanUtils {

    private BooleanUtils() {}

    public static boolean toBoolean(String value) {
        return value.equalsIgnoreCase("Y");
    }

}
