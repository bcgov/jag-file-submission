package ca.bc.gov.ca.open.jag.bambora;

import ca.bc.gov.open.jag.bambora.BamboraProperties;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("BamboraProperties")
public class BamboraPropertiesTest {

    private static final String API_KEY = "AVALUE";

    @Test
    public void testProperties() {
        BamboraProperties bamboraProperties = new BamboraProperties();
        bamboraProperties.setApiKey(API_KEY);

        Assertions.assertEquals(API_KEY, bamboraProperties.getApiKey());
    }
}
