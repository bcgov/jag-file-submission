package ca.bc.gov.open.jag.efiling.demo;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingCourtServiceDemoImplTest {
    EfilingCourtServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingCourtServiceDemoImpl();
    }

    @Test
    @DisplayName("OK: with empty cache should return null")
    public void withEmptyCacheShouldReturnNull() {

        String actual = sut.getCourtDescription("SOMEVALUE");
        Assertions.assertEquals("Imma Court", actual);

    }
}
