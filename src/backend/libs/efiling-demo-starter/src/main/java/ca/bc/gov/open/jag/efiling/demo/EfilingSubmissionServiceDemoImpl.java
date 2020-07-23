package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import java.math.BigDecimal;
import java.util.UUID;

public class EfilingSubmissionServiceDemoImpl implements EfilingSubmissionService {
    @Override
    public BigDecimal submitFilingPackage(UUID stuff) {
        return BigDecimal.ONE;
    }
}
