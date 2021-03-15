package ca.bc.gov.open.jag.efilingcsoclient;


import ca.bc.gov.ag.csows.lookups.CodeValue;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsoLookupServiceImpl implements EfilingLookupService {

    private LookupFacadeBean lookupFacadeItf;

    public CsoLookupServiceImpl(LookupFacadeBean lookupFacadeItf) {
        this.lookupFacadeItf = lookupFacadeItf;
    }

    @Override
    public ServiceFees getServiceFee(String serviceId)  {

        // NOTE- "DCFL" is the only string that will work here until we get our service types setup
        if (StringUtils.isEmpty(serviceId)) throw new IllegalArgumentException("service Id is required");

        try {
            ServiceFee fee = lookupFacadeItf.getServiceFee(serviceId, CsoHelpers.date2XMLGregorian(new Date()));
            if (fee == null)
                throw new EfilingLookupServiceException("Fee not found");

            return new ServiceFees(
                    fee.getFeeAmt(),
                    fee.getServiceTypeCd());

        }
        catch(DatatypeConfigurationException | NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while retrieving service fee", e.getCause());
        }

    }

    @Override
    public List<String> getValidPartyRoles(String courtLevel, String courtClass, String documentTypes) {
        try {
            List<CodeValue> partyRolesResponse = lookupFacadeItf.getEfilingPartyRoles(courtLevel, courtClass, documentTypes);
            List<String> validRoles = new ArrayList<>();

            for (CodeValue partyRole : partyRolesResponse) {
                validRoles.add(partyRole.getCode());
            }

            return validRoles;

        } catch(NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while getting party roles", e.getCause());
        }
    }
}
