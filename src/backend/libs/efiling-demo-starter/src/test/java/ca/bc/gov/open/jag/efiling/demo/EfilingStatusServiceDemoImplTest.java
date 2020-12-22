package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.*;

public class EfilingStatusServiceDemoImplTest {
    EfilingStatusServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingStatusServiceDemoImpl();
    }

    @Test
    @DisplayName("OK: with empty cache should return null")
    public void withEmptyCacheShouldReturnNull() {
        Assertions.assertFalse(sut.findStatusByPackage(null).isPresent());

    }
}
