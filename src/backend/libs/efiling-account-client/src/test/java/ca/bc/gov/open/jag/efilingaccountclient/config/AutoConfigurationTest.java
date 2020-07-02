package ca.bc.gov.open.jag.efilingaccountclient.config;



import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.ArrayList;

@DisplayName("AutoConfiguration Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoConfigurationTest {
    private static final String URI = "URI";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(AutoConfiguration.class);

    private AutoConfiguration sut;

    @Test
    @DisplayName("Test that beans are created")
    public void testBeansAreGenerated() {

        sut = new AutoConfiguration(initSoapProperties());

        Assertions.assertNotNull(sut.accountFacadeBean());
        Assertions.assertNotNull(sut.roleRegistryPortType());
        Assertions.assertNotNull(sut.bCeIDServiceSoap());
        Assertions.assertNotNull(sut.efilingAccountService(null, null, null, null));
    }

    private SoapProperties initSoapProperties() {

        ArrayList<EfilingSoapClientProperties> soapClientProperties = new ArrayList<>();

        EfilingSoapClientProperties accountProperties = new EfilingSoapClientProperties();
        accountProperties.setClient(Clients.ACCOUNT);
        accountProperties.setUri(URI);
        accountProperties.setUserName(USERNAME);
        accountProperties.setPassword(PASSWORD);
        soapClientProperties.add(accountProperties);


        EfilingSoapClientProperties roleProperties = new EfilingSoapClientProperties();
        roleProperties.setClient(Clients.ROLE);
        roleProperties.setUri(URI);
        roleProperties.setUserName(USERNAME);
        roleProperties.setPassword(PASSWORD);
        soapClientProperties.add(roleProperties);

        EfilingSoapClientProperties bceidProperties = new EfilingSoapClientProperties();
        bceidProperties.setClient(Clients.BCEID);
        bceidProperties.setUri(URI);
        bceidProperties.setUserName(USERNAME);
        bceidProperties.setPassword(PASSWORD);
        soapClientProperties.add(bceidProperties);

        SoapProperties soapProperties = new SoapProperties();
        soapProperties.setClients(soapClientProperties);

        return soapProperties;
    }
}
