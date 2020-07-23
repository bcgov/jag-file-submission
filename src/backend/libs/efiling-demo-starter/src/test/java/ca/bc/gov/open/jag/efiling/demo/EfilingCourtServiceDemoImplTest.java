package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

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

        CourtDetails actual = sut.getCourtDescription("SOMEVALUE");
        Assertions.assertEquals("Imma Court", actual.getCourtDescription());
        Assertions.assertEquals("Imma Level", actual.getLevelDescription());
        Assertions.assertEquals("Imma Class", actual.getClassDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCourtId());
    }
}
