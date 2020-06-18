package ca.bc.gov.open.jagefilingapi.qa.tests;

import ca.bc.gov.open.jagefilingapi.qa.restgenerateurl.GenerateUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

public class GenerateUrlTest {
    GenerateUrl generateUrl;

    @Test
    public void verifyEfilingUrlIsGeneratedTest() throws IOException {
        generateUrl = new GenerateUrl();

        generateUrl.generateEfilingUrl();
    }
}
