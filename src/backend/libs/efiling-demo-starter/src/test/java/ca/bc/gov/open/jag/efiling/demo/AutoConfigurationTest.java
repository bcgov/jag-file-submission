package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.service.*;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoConfigurationTest {

    private AutoConfiguration sut;

    @BeforeAll
    public void setUp() {
        sut = new AutoConfiguration();
    }

    @Test
    @DisplayName("OK: AutoConfiguration should return instance of EfilingAccountServiceDemoImpl")
    public void autoConfigurationShouldReturnDemoAccountServiceImpl() {

        EfilingAccountService actual = sut.efilingAccountService();
        Assertions.assertEquals(EfilingAccountServiceDemoImpl.class, actual.getClass());
    }

    @Test
    @DisplayName("OK: AutoConfiguration should return instance of EfilingLookupServiceDemoImpl")
    public void autoConfigurationShouldReturnEfilingLookupServiceDemoImpl() {

        EfilingLookupService actual = sut.efilingLookupService();
        Assertions.assertEquals(EfilingLookupServiceDemoImpl.class, actual.getClass());
        
    }

    @Test
    @DisplayName("OK: AutoConfiguration should return instance of EfilingocumentServiceDemoImpl")
    public void autoConfigurationShouldReturnEfilingDocumentServiceDemoImpl() {

        EfilingDocumentService actual = sut.efilingDocumentService();
        Assertions.assertEquals(EfilingDocumentServiceDemoImpl.class, actual.getClass());

    }

    @Test
    @DisplayName("OK: AutoConfiguration should return instance of EfilingCourtServiceDemoImpl")
    public void autoConfigurationShouldReturnEfilingCourtServiceDemoImpl() {

        EfilingCourtService actual = sut.efilingCourtService();
        Assertions.assertEquals(EfilingCourtServiceDemoImpl.class, actual.getClass());

    }

    @Test
    @DisplayName("OK: AutoConfiguration should return instance of EfilingSubmissionServiceDemoImpl")
    public void autoConfigurationShouldReturnEfilingSubmissionServiceDemoImpl() {

        EfilingSubmissionService actual = sut.efilingSubmissionService();
        Assertions.assertEquals(EfilingSubmissionServiceDemoImpl.class, actual.getClass());

    }

}
