package ca.bc.gov.open.jag.efilingapi.filingpackage.util;

import ca.bc.gov.open.jag.efilingapi.api.model.Rush;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetRushTypeTest")
public class GetRushTypeTest {

    @Test
    @DisplayName("CRTD maps to Court")
    public void testMappingCourt() {

        Assertions.assertEquals(Rush.RushTypeEnum.COURT, RushMappingUtils.getRushType("CRTD"));

    }

    @Test
    @DisplayName("CRTR maps to Rule")
    public void testMappingRule() {

        Assertions.assertEquals(Rush.RushTypeEnum.RULE, RushMappingUtils.getRushType("CRTR"));

    }

    @Test
    @DisplayName("OTHR maps to Other")
    public void testMappingOther() {

        Assertions.assertEquals(Rush.RushTypeEnum.OTHER, RushMappingUtils.getRushType("OTHR"));

    }

    @Test
    @DisplayName("PRO maps to PRO")
    public void testMappingPro() {

        Assertions.assertEquals(Rush.RushTypeEnum.PRO, RushMappingUtils.getRushType("PRO"));

    }


    @Test
    @DisplayName("Unknown maps to null")
    public void testMappingUnknown() {

        Assertions.assertNull(RushMappingUtils.getRushType("UNKOWN"));

    }


    @Test
    @DisplayName("null maps to null")
    public void testMappingNull() {

        Assertions.assertNull(RushMappingUtils.getRushType(null));

    }

}
