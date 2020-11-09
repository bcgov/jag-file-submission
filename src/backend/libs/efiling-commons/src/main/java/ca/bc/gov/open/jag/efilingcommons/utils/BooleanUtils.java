package ca.bc.gov.open.jag.efilingcommons.utils;

import org.springframework.util.StringUtils;

public class BooleanUtils {

    private BooleanUtils() {}

    public static Boolean toBoolean(String value) {
        if (StringUtils.isEmpty(value)) return null;

        return (value.trim().toLowerCase().startsWith("y") ?
                Boolean.TRUE : Boolean.FALSE
        );
    }

}
