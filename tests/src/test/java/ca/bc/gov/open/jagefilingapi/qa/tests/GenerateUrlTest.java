package ca.bc.gov.open.jagefilingapi.qa.tests;

import ca.bc.gov.open.jagefilingapi.qa.restgenerateurl.GenerateUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

public class GenerateUrlTest {
    GenerateUrl generateUrl;

    @Test
    public void verifyEfilingUrlIsGeneratedTest() throws JsonProcessingException {
        generateUrl = new GenerateUrl();

        generateUrl.generateEfilingUrl();
    }
}
