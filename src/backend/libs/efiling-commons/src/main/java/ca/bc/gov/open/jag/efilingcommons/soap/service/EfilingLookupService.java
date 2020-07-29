package ca.bc.gov.open.jag.efilingcommons.soap.service;


import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;

public interface EfilingLookupService {

    ServiceFees getServiceFee(String serviceId);

}
