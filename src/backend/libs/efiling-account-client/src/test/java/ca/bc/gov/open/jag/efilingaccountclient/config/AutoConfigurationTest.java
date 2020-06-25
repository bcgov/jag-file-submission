package ca.bc.gov.open.jag.efilingaccountclient.config;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {


        CsoAccountProperties accountProperties = new CsoAccountProperties();
        accountProperties.setUri(URI);
        accountProperties.setUserName(USERNAME);
        accountProperties.setPassword(PASSWORD);

        CsoRoleProperties roleProperties = new CsoRoleProperties();
        roleProperties.setUri(URI);
        roleProperties.setUserName(USERNAME);
        roleProperties.setPassword(PASSWORD);

        sut = new AutoConfiguration(accountProperties, roleProperties);

        Assertions.assertNotNull(sut.accountFacadeBean());
        Assertions.assertNotNull(sut.roleRegistryPortType());
        Assertions.assertNotNull(sut.efilingAccountService(null, null));
    }
}
