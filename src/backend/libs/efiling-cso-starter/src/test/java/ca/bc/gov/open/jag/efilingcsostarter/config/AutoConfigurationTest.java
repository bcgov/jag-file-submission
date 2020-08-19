package ca.bc.gov.open.jag.efilingcsostarter.config;


import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import ca.bc.gov.open.jag.efilingcsostarter.*;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.AccountDetailsMapperImpl;
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

    @BeforeAll
    public void setup() {
        sut = new AutoConfiguration(initSoapProperties(), csoProperties);
    }

    @Test
    @DisplayName("Test that beans are created")
    public void testBeansAreGenerated() {


        Assertions.assertNotNull(sut.accountFacadeBean());
        Assertions.assertNotNull(sut.roleRegistryPortType());
        Assertions.assertEquals(CsoAccountServiceImpl.class, sut.efilingAccountService(null, null, null).getClass());
        Assertions.assertEquals(CsoDocumentServiceImpl.class, sut.efilingDocumentService(null).getClass());
        Assertions.assertEquals(CsoLookupServiceImpl.class, sut.efilingLookupService(null).getClass());
        Assertions.assertEquals(CsoCourtServiceImpl.class, sut.efilingCourtService(null).getClass());
        Assertions.assertEquals(CsoSubmissionServiceImpl.class, sut.efilingSubmissionService(null,null, null, null, null).getClass());
        Assertions.assertEquals(AccountDetailsMapperImpl.class, sut.accountDetailsMapper().getClass());

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

        SoapProperties soapProperties = new SoapProperties();
        soapProperties.setClients(soapClientProperties);

        return soapProperties;
    }
}
