package ca.bc.gov.open.jag.efilingcommons.utils;

import org.joda.time.DateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    private DateUtils() {}

    public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
        GregorianCalendar entryDate = new GregorianCalendar();
        entryDate.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(entryDate);
    }

    public static XMLGregorianCalendar getCurrentXmlDate() {
        try {
            return getXmlDate(DateTime.now().toDate());
        } catch (DatatypeConfigurationException e) {
            //This will never execute
            throw new RuntimeException("Impossible");
        }
    }

}
