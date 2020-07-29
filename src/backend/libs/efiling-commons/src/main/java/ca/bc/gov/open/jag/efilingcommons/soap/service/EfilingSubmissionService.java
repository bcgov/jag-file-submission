package ca.bc.gov.open.jag.efilingcommons.soap.service;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;

import java.math.BigDecimal;
import java.util.UUID;

public interface EfilingSubmissionService {
    BigDecimal submitFilingPackage(UUID submissionId);

    EfilingService addService(EfilingService efilingService);
}
