package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;

public class EfilingCourtServiceDemoImpl implements EfilingCourtService {
    @Override
    public String getCourtDescription(String agencyIdentifierCd) {
        return "Imma Court";
    }
}
