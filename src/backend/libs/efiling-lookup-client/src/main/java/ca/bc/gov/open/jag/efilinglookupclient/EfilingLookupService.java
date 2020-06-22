package ca.bc.gov.open.jag.efilinglookupclient;


import javax.xml.datatype.DatatypeConfigurationException;

public interface EfilingLookupService {

    public ServiceFees getServiceFee(String serviceId) throws DatatypeConfigurationException;
}
