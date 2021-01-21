package ca.bc.gov.open.jag.efilingcsoclient;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Helper class for CSO
 */
public class CsoHelpers {

    private CsoHelpers() {}

    /**
     * Converts UUID to CSO ID FORMAT: no hyphen and all upper case.
     * @param id
     * @return
     */
    public static String formatUserGuid(String id) {
        return id.replace("-", "").toUpperCase();
    }

    /**
     * Helper function to convert a Date to an XMLGregorianCalendar date for sending to SOAP
     * @param date
     * @return XMLGregorianCalendar
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar date2XMLGregorian(Date date) throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }
}
