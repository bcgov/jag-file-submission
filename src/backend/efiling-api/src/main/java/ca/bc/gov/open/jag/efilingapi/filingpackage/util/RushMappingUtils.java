package ca.bc.gov.open.jag.efilingapi.filingpackage.util;

import ca.bc.gov.open.jag.efilingapi.api.model.Rush;

public class RushMappingUtils {

    private RushMappingUtils() {}

    public static Rush.RushTypeEnum getRushType(String code) {

        if (code == null) return null;

        switch (code.toUpperCase()) {
            case "CRTD":
                return Rush.RushTypeEnum.COURT;
            case "CRTR":
                return Rush.RushTypeEnum.RULE;
            case "OTHR":
                return Rush.RushTypeEnum.OTHER;
            case "PRO":
                return Rush.RushTypeEnum.PRO;
            default:
                return null;
        }

    }

}
