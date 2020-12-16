package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.FilePackage;

import java.math.BigDecimal;

public interface EfilingStatusService {

    FilePackage findStatusByPackage(BigDecimal clientId, BigDecimal packageNo);

}
