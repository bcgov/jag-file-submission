package ca.bc.gov.open.jag.efilingcommons.submission;

import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;

import java.math.BigDecimal;
import java.util.Optional;

public interface EfilingStatusService {

    Optional<FilingPackage> findStatusByPackage(BigDecimal clientId, BigDecimal packageNo);

}
