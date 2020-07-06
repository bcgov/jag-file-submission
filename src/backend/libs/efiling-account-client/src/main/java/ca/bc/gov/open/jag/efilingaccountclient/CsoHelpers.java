package ca.bc.gov.open.jag.efilingaccountclient;

import java.util.UUID;

/**
 * Helper class for CSO
 */
public class CsoHelpers {

    private CsoHelpers() {}

    /**
     * Converts UUID to CSO ID FORMAT: no hyphen and all upper case.
     * @param id
     * @return
     */
    public static String formatUserGuid(UUID id) {
        return id.toString().replace("-", "").toUpperCase();
    }

}
