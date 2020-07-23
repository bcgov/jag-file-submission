package ca.bc.gov.open.jag.efilingcommons.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface EfilingSubmissionService {
    BigDecimal submitFilingPackage(UUID submissionId);
}
