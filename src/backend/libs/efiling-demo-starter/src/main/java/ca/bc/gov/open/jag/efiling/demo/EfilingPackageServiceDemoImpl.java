package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFee;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPackageService;

import java.math.BigDecimal;

public class EfilingPackageServiceDemoImpl implements EfilingPackageService {

    @Override
    public SubmissionFee getSubmissionFee(String serviceId) {

        return  new SubmissionFee(
                BigDecimal.valueOf(7),
                "serviceTypeCd");

    }
}
