package ca.bc.gov.open.jag.efilingcommons.service;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;

import java.util.List;

public interface EfilingLookupService {

    ServiceFees getServiceFee(String serviceId);

    List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes);
}
