package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingCourtService;

import java.math.BigDecimal;

public class EfilingCourtServiceDemoImpl implements EfilingCourtService {
    @Override
    public CourtDetails getCourtDescription(String agencyIdentifierCd) {
        return new CourtDetails(BigDecimal.TEN, "Imma Court", "Imma Class", "Imma Level");
    }
}
