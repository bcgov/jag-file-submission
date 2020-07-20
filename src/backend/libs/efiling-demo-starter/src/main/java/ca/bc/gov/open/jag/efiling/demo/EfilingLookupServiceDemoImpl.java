package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;

import java.math.BigDecimal;

public class EfilingLookupServiceDemoImpl implements EfilingLookupService {

    @Override
    public ServiceFees getServiceFee(String serviceId) {

        return  new ServiceFees(
                BigDecimal.valueOf(7),
                "serviceTypeCd");

    }
}
