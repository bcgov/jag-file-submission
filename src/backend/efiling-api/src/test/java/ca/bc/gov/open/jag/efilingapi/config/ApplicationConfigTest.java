package ca.bc.gov.open.jag.efilingapi.config;

import ca.bc.gov.open.jag.efilinglookupclient.DemoLookupServiceImpl;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationConfigTest {


    private ApplicationConfig sut;


    @BeforeAll
    public void setup() {
        sut= new ApplicationConfig();
    }

    @Test
    @DisplayName("CASE 1: testing demo config")
    public void configurationShouldReturnDemoLookupServiceImpl() {


        Assertions.assertEquals(DemoLookupServiceImpl.class, sut.efilingLookupService().getClass());


    }


}
