package ca.bc.gov.open.jag.efilingstatusclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class CSOStatusServiceImpl extends WebServiceGatewaySupport implements EfilingStatusService {

    private static final Logger _logger = LoggerFactory.getLogger(CSOStatusServiceImpl.class);

}
