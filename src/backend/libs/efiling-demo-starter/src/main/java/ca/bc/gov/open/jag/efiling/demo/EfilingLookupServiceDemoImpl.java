package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFeeRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EfilingLookupServiceDemoImpl implements EfilingLookupService {

    @Override
    public List<LookupItem> getCountries() {
        return Arrays.asList(
                LookupItem.builder().code("1").description("Canada").create(),
                LookupItem.builder().code("1").description("United States").create(),
                LookupItem.builder().code("34").description("Spain").create());
    }

    @Override
    public ServiceFees getServiceFee(SubmissionFeeRequest submissionFeeRequest) {

        return  new ServiceFees(
                BigDecimal.valueOf(7),
                "serviceTypeCd");

    }

    @Override
    public List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes, String division) {
        List<String> validRoles = new ArrayList<>();
        validRoles.add("APP");

        return validRoles;
    }
}
