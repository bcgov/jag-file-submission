package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.ag.csows.filing.FilingPackage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class DemoSubmissionServiceImpl implements EfilingSubmissionService {

    @Override
    public BigDecimal submitFiling(FilingPackage filingPackage) {

        return BigDecimal.ONE;
    }
}
