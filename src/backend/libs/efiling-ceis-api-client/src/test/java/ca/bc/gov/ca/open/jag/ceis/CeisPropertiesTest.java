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

    @Test
    public void testProperties() {
        CeisProperties bamboraProperties = new CeisProperties();
        bamboraProperties.setCeisBasePath(BASEPATH);
        Assertions.assertEquals(BASEPATH, bamboraProperties.getCeisBasePath());
    }

}
