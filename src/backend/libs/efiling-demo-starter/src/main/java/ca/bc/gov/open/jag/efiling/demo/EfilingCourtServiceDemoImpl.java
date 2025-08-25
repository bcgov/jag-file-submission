package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;

import java.math.BigDecimal;
import java.util.Optional;

public class EfilingCourtServiceDemoImpl implements EfilingCourtService {
    @Override
    public Optional<CourtDetails> getCourtDescription(String agencyIdentifierCd, String courtLevel, String courtClass) {
        return Optional.of(new CourtDetails(BigDecimal.TEN, "Imma Court", "Imma Class", "Imma Level"));
    }

    @Override
    public boolean checkValidLevelClassLocation(BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode) {
        return true;
    }

    @Override
    public boolean checkValidCourtFileNumber(String fileNumber, BigDecimal agencyId, String courtLevel, String courtClass, String applicationCode) {
        return true;
    }
}
