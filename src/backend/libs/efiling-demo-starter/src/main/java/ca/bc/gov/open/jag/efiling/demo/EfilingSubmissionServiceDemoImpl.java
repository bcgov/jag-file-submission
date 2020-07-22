package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;

import java.math.BigDecimal;

public class EfilingSubmissionServiceDemoImpl implements EfilingSubmissionService {
    @Override
    public BigDecimal submitFilingPackage(String stuff) {
        return BigDecimal.ONE;
    }
}
