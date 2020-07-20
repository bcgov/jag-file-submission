package ca.bc.gov.open.jag.efilinglookupclient;


import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

public class CSOLookupServiceImpl implements EfilingLookupService {

    private LookupFacadeBean lookupFacadeItf;

    public CSOLookupServiceImpl(LookupFacadeBean lookupFacadeItf) {
        this.lookupFacadeItf = lookupFacadeItf;
    }

    @Override
    public ServiceFees getServiceFee(String serviceId)  {

        // NOTE- "DCFL" is the only string that will work here until we get our service types setup
        // TODO: throw exception when service fee is null
        if (StringUtils.isEmpty(serviceId)) throw new IllegalArgumentException("service Id is required");

        try {
            ServiceFee fee = lookupFacadeItf.getServiceFee(serviceId, date2XMLGregorian(new Date()));
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

    /**
     * Helper function to convert a Date to an XMLGregorianCalendar date for sending to SOAP
     * @param date
     * @return XMLGregorianCalendar
     * @throws DatatypeConfigurationException
     */
    private XMLGregorianCalendar date2XMLGregorian(Date date) throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

}
