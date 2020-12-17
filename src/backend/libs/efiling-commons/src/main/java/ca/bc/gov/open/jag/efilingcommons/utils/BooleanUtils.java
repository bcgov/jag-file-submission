package ca.bc.gov.open.jag.efilingcommons.utils;


import org.apache.commons.lang3.StringUtils;

public class BooleanUtils {

    private BooleanUtils() {}

    public static boolean toBoolean(String value) {
        return StringUtils.equalsIgnoreCase(value, "Y");
    }

}
