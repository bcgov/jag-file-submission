package ca.bc.gov.open.jag.efilinglookupclient.config;

import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.ArrayList;

@DisplayName("AutoConfiguration Test")
public class AutoConfigurationTest {
    private static final String URI = "URI";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(AutoConfiguration.class);


    private AutoConfiguration sut;

   // @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {
        ArrayList<EfilingSoapClientProperties> soapClientProperties = new ArrayList<>();

        EfilingSoapClientProperties lookupProperties = new EfilingSoapClientProperties();
        lookupProperties.setClient(Clients.LOOKUP);
        lookupProperties.setUri(URI);
        lookupProperties.setUserName(USERNAME);
        lookupProperties.setPassword(PASSWORD);
        soapClientProperties.add(lookupProperties);

        SoapProperties soapProperties = new SoapProperties();
        soapProperties.setClients(soapClientProperties);

        sut = new AutoConfiguration(soapProperties);

        Assertions.assertNotNull(sut.lookupFacadeItf());
        Assertions.assertNotNull(sut.efilingLookupService(null));
    }
}
