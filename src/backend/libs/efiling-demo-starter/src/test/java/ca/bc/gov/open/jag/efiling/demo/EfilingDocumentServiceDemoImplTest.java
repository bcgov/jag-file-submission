package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Stats Client Test Suite")
public class EfilingDocumentServiceDemoImplTest {
    @DisplayName("CASE 1: Testing Demo getDocumentDetails")
    @Test
    public void testDemoLookupServiceTest() {

        EfilingDocumentServiceDemoImpl service = new EfilingDocumentServiceDemoImpl();

        String serviceId = "TestServiceId";
        DocumentDetails actual = service.getDocumentDetails(serviceId);

        Assertions.assertEquals("This is a doc", actual.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(7), actual.getStatutoryFeeAmount());
    }
}
