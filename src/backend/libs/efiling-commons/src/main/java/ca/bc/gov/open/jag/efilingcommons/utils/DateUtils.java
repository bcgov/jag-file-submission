package ca.bc.gov.open.jag.efilingcommons.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    private DateUtils() {}

    public static XMLGregorianCalendar getXmlDate(Date date) {
        GregorianCalendar entryDate = new GregorianCalendar();
        entryDate.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(entryDate);
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

}
