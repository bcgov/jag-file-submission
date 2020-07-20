package ca.bc.gov.open.jag.efilingcsostarter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

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
    public static String formatUserGuid(UUID id) {
        return id.toString().replace("-", "").toUpperCase();
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
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        return date2;
    }
}
