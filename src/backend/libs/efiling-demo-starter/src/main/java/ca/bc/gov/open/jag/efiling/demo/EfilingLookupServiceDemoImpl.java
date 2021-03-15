package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EfilingLookupServiceDemoImpl implements EfilingLookupService {

    @Override
    public ServiceFees getServiceFee(SubmissionFeeRequest submissionFeeRequest) {

        return  new ServiceFees(
                BigDecimal.valueOf(7),
                "serviceTypeCd");

    }

    @Override
    public List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes) {
        List<String> validRoles = new ArrayList<>();
        validRoles.add("APP");

        return validRoles;
    }
}
