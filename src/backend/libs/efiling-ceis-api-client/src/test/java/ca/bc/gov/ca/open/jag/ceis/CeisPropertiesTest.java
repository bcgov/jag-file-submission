package ca.bc.gov.ca.open.jag.ceis;

import ca.bc.gov.open.jag.ceis.CeisProperties;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CeisProperties")
public class CeisPropertiesTest {

    private static final String BASEPATH = "AVALUE";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Test
    public void testProperties() {
        CeisProperties bamboraProperties = new CeisProperties();
        bamboraProperties.setCeisBasePath(BASEPATH);
        bamboraProperties.setCeisUsername(USERNAME);
        bamboraProperties.setCeisPassword(PASSWORD);
        Assertions.assertEquals(BASEPATH, bamboraProperties.getCeisBasePath());
        Assertions.assertEquals(USERNAME, bamboraProperties.getCeisUsername());
        Assertions.assertEquals(PASSWORD, bamboraProperties.getCeisPassword());
    }

}
