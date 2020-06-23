package ca.bc.gov.open.jag.efilingstatusclient.config;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AutoConfiguration Test")
public class CSOStatusPropertiesTest {

    private static final String SOMEURL = "someurl";

    @Test
    @DisplayName("CASE1: test properties")
    public void testProperties() {
        CSOStatusProperties result = new CSOStatusProperties();
        result.setFilingStatusSoapUri(SOMEURL);

        Assertions.assertEquals(SOMEURL, result.getFilingStatusSoapUri());
    }
}
