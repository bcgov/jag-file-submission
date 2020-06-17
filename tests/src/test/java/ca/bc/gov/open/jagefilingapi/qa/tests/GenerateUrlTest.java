package ca.bc.gov.open.jagefilingapi.qa.tests;

import ca.bc.gov.open.jagefilingapi.qa.restgenerateurl.GenerateUrl;
import org.junit.Test;

public class GenerateUrlTest {
    GenerateUrl generateUrl;

    @Test
    public void verifyEfilingUrlIsGeneratedTest() {
        generateUrl = new GenerateUrl();

        generateUrl.generateEfilingUrl();
    }
}
