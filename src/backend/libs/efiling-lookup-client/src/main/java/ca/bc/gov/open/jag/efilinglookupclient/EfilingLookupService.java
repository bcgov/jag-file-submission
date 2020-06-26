package ca.bc.gov.open.jag.efilinglookupclient;


import ca.bc.gov.ag.csows.lookups.ServiceFee;

import javax.xml.datatype.DatatypeConfigurationException;

public interface EfilingLookupService {

    public ServiceFee getServiceFee(String serviceId) throws DatatypeConfigurationException;
}
