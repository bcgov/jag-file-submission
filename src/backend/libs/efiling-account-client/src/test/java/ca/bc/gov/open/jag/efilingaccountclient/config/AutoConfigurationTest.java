package ca.bc.gov.open.jag.efilingaccountclient.config;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

@DisplayName("AutoConfiguration Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoConfigurationTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(AutoConfiguration.class);


    private AutoConfiguration sut;


    @Test
    @DisplayName("CASE1: Test that beans are created")
    public void testBeansAreGenerated() {


        CsoAccountProperties properties = new CsoAccountProperties();
        properties.setFilingAccountSoapUri("test");
        sut = new AutoConfiguration(properties);

        Assertions.assertNotNull(sut.accountFacadeBean());
        Assertions.assertNotNull(sut.roleRegistryPortType());
        Assertions.assertNotNull(sut.efilingAccountService(null));
    }
}
