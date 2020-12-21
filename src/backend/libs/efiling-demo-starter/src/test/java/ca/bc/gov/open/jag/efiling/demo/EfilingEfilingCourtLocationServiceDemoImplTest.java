package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingEfilingCourtLocationServiceDemoImplTest {
    @DisplayName("Test list returned")
    @Test
    public void testDemoCourtLookupServiceTest() {

        EfilingCourtLocationServiceDemoImpl service = new EfilingCourtLocationServiceDemoImpl();

        List<InternalCourtLocation> actual = service.getCourtLocations("TYPE");

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(10264), actual.get(0).getId());
        Assertions.assertEquals("5871", actual.get(0).getIdentifierCode());
        Assertions.assertEquals("Campbell River", actual.get(0).getName());
        Assertions.assertEquals("OMH", actual.get(0).getCode());
        Assertions.assertEquals(true, actual.get(0).getIsSupremeCourt());
        Assertions.assertEquals("500 - 13th Avenue", actual.get(0).getAddress().getAddressLine1());
        Assertions.assertNull(actual.get(0).getAddress().getAddressLine2());
        Assertions.assertNull(actual.get(0).getAddress().getAddressLine3());
        Assertions.assertEquals("V9W 6P1", actual.get(0).getAddress().getPostalCode());
        Assertions.assertEquals("Campbell River", actual.get(0).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(0).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(0).getAddress().getCountryName());

        Assertions.assertEquals(BigDecimal.valueOf(9393), actual.get(1).getId());
        Assertions.assertEquals("3561", actual.get(1).getIdentifierCode());
        Assertions.assertEquals("Chilliwack", actual.get(1).getName());
        Assertions.assertEquals("ABB", actual.get(1).getCode());
        Assertions.assertEquals(true, actual.get(1).getIsSupremeCourt());
        Assertions.assertEquals("46085 Yale Road", actual.get(1).getAddress().getAddressLine1());
        Assertions.assertEquals("  ", actual.get(1).getAddress().getAddressLine2());
        Assertions.assertEquals("  ", actual.get(1).getAddress().getAddressLine3());
        Assertions.assertEquals("V2P 2L8", actual.get(1).getAddress().getPostalCode());
        Assertions.assertEquals("Chilliwack", actual.get(1).getAddress().getCityName());
        Assertions.assertEquals("British Columbia", actual.get(1).getAddress().getProvinceName());
        Assertions.assertEquals("Canada", actual.get(1).getAddress().getCountryName());

    }
}
