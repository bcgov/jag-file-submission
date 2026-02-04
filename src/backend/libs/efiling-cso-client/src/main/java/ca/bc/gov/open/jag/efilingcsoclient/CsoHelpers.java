package ca.bc.gov.open.jag.efilingcsoclient;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
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

    /**
     * Helper function to convert an XMLGregorianCalendar date to a Date
     * @param gCalendar
     * @return Date
     */
    public static Date xmlGregorian2Date(XMLGregorianCalendar gCalendar) {

        if (gCalendar != null) {
            Calendar calendarInstance = Calendar.getInstance();
            calendarInstance.setTime(gCalendar.toGregorianCalendar().getTime());
            return calendarInstance.getTime();
        }
        return null;
    }
}
