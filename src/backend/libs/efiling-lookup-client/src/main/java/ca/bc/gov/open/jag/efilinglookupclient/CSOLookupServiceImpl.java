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
                return new ServiceFees(null, BigDecimal.ZERO, null, serviceId, null, null, null, null);

            return new ServiceFees(
                    toJoda(fee.getUpdDtm()).toString(),
                    fee.getFeeAmt(),
                    fee.getEntUserId(),
                    fee.getServiceTypeCd(),
                    toJoda(fee.getEffectiveDt()).toString(),
                    fee.getUpdUserId(),
                    toJoda(fee.getEntDtm()).toString(),
                    toJoda(fee.getExpiryDt()).toString());

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
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        return date2;
    }

    private DateTime toJoda(XMLGregorianCalendar date) {
        if (date != null) {
            return DateTime.parse(date.toString());
        }
        return new DateTime();
    }

}
