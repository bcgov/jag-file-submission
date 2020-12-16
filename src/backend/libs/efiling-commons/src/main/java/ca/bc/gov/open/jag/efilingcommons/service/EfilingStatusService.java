package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.FilePackage;

import java.math.BigDecimal;
import java.util.Optional;

public interface EfilingStatusService {

    Optional<FilePackage> findStatusByPackage(BigDecimal clientId, BigDecimal packageNo);

}
