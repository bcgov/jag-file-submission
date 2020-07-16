package ca.bc.gov.open.jag.efilingcommons.service;


import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;

import javax.xml.datatype.DatatypeConfigurationException;

public interface EfilingLookupService {

    ServiceFees getServiceFee(String serviceId);

}
