package ca.bc.gov.open.jag.efilingcsostarter;


import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFee;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPackageService;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Date;
public class CsoPackageServiceImpl implements EfilingPackageService {

    private LookupFacadeBean lookupFacadeItf;

    public CsoPackageServiceImpl(LookupFacadeBean lookupFacadeItf) {
        this.lookupFacadeItf = lookupFacadeItf;
    }

    @Override
    public SubmissionFee getSubmissionFee(String serviceId)  {

        // NOTE- "DCFL" is the only string that will work here until we get our service types setup
        if (StringUtils.isEmpty(serviceId)) throw new IllegalArgumentException("service Id is required");

        try {
            ServiceFee fee = lookupFacadeItf.getServiceFee(serviceId, CsoHelpers.date2XMLGregorian(new Date()));
            if (fee == null)
                throw new EfilingLookupServiceException("Fee not found");

            return new SubmissionFee(
                    fee.getFeeAmt(),
                    fee.getServiceTypeCd());

        }
        catch(DatatypeConfigurationException | NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while retrieving service fee", e.getCause());
        }

    }
}
