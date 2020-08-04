package ca.bc.gov.open.jag.efilingcommons.model.utils;

import org.joda.time.DateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class DateUtilsTest {
    @Test
    @DisplayName("Ok")
    public void withProperDateReturnWMLDate() {
       Date DATE = DateTime.parse("2018-05-05T11:50:55").toDate();
    }
}
