package ca.bc.gov.open.jag.efilinglookupclient;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class CSOLookupServiceImpl extends WebServiceGatewaySupport implements EfilingLookupService {

    @Override
    public ServiceFees getServiceFee(String serviceId) {
        return null;
    }
}
