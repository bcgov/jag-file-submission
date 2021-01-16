package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Stats Client Test Suite")
public class EfilingDocumentServiceDemoImplTest {
    @DisplayName("CASE 1: Testing Demo getDocumentDetails")
    @Test
    public void testDemoLookupServiceTest() {

        EfilingDocumentServiceDemoImpl service = new EfilingDocumentServiceDemoImpl();

        String serviceId = "TestServiceId";
        DocumentDetails actual = service.getDocumentDetails(serviceId, Mockito.any(), Mockito.any());

        Assertions.assertEquals("This is a doc", actual.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(7), actual.getStatutoryFeeAmount());
        Assertions.assertTrue(actual.getOrderDocument());
    }
    @DisplayName("CASE 2: Testing Demo getDocumentTypes")
    @Test
    public void testDemoDocumentServiceTest() {

        EfilingDocumentServiceDemoImpl service = new EfilingDocumentServiceDemoImpl();

        List<DocumentType> actual = service.getDocumentTypes(Mockito.any(), Mockito.any());

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("Description1", actual.get(0).getDescription());
        Assertions.assertEquals("AFF", actual.get(0).getType());
    }
}
