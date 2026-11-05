package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SoapUtilsTest {

    @Test
    @DisplayName("OK: should create a soap port")
    public void withValidConfigShouldCreateSoapPort() {

        EfilingSoapClientProperties client = new EfilingSoapClientProperties();
        client.setClient(Clients.STATUS);
        client.setUserName("username");
        client.setPassword("password");
        client.setUri("http://localhost:8080");
        CsoProperties csoProperties = new CsoProperties();
        csoProperties.setDebugEnabled(false);

        Assertions.assertDoesNotThrow(() -> SoapUtils.getPort(FilingStatusFacadeBean.class, client, false));


    }

    @Test
    @DisplayName("OK: should create a soap port with logging enabled")
    public void withValidConfigShouldCreateSoapPortWithLoggingEnabled() {

        EfilingSoapClientProperties client = new EfilingSoapClientProperties();
        client.setClient(Clients.STATUS);
        client.setUserName("username");
        client.setPassword("password");
        client.setUri("http://localhost:8080");
        CsoProperties csoProperties = new CsoProperties();
        csoProperties.setDebugEnabled(true);

        Assertions.assertDoesNotThrow(() -> SoapUtils.getPort(FilingStatusFacadeBean.class, client, true));


    }

}
