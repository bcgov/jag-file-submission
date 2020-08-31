package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import org.mapstruct.Named;

import javax.xml.datatype.XMLGregorianCalendar;

@Named("DateMapper")
public interface DateMapper {

    @Named("toCurrentDateTime")
    static XMLGregorianCalendar toCurrentXmlDate() {
        return DateUtils.getCurrentXmlDate();
    }

}

