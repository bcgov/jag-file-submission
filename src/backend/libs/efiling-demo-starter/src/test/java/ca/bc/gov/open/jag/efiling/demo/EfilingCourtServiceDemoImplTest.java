package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingCourtServiceDemoImplTest {
    private static final String SOMEVALUE = "SOMEVALUE";
    EfilingCourtServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingCourtServiceDemoImpl();
    }

    @Test
    @DisplayName("OK: with empty cache should return a value")
    public void withEmptyCacheShouldReturnAValue() {

        Optional<CourtDetails> actual = sut.getCourtDescription(SOMEVALUE, SOMEVALUE, SOMEVALUE);
        Assertions.assertEquals("Imma Court", actual.get().getCourtDescription());
        Assertions.assertEquals("Imma Level", actual.get().getLevelDescription());
        Assertions.assertEquals("Imma Class", actual.get().getClassDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.get().getCourtId());
    }
}
