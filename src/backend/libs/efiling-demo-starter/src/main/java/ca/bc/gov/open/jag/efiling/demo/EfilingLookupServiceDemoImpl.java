package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class EfilingLookupServiceDemoImpl implements EfilingLookupService {

    @Override
    public ServiceFees getServiceFee(String serviceId) {

        return  new ServiceFees(DateTime.now().toString(),
                BigDecimal.valueOf(7),
                "entUserId",
                "serviceTypeCd",
                DateTime.now().toString(),
                "updUserId",
                DateTime.now().toString(),
                DateTime.now().toString());

    }
}
