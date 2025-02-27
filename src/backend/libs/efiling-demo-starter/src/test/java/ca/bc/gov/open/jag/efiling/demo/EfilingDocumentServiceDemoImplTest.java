package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
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
        DocumentTypeDetails actual = service.getDocumentTypeDetails(serviceId, Mockito.anyString(), Mockito.anyString());

        Assertions.assertEquals("This is a doc", actual.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(7), actual.getStatutoryFeeAmount());
        Assertions.assertTrue(actual.isOrderDocument());
        Assertions.assertTrue(actual.isRushRequired());
        Assertions.assertFalse(actual.isAutoProcessing());

    }
    @DisplayName("CASE 2: Testing Demo getDocumentTypes")
    @Test
    public void testDemoDocumentServiceTest() {

        EfilingDocumentServiceDemoImpl service = new EfilingDocumentServiceDemoImpl();

        List<DocumentTypeDetails> actual = service.getDocumentTypes(Mockito.anyString(), Mockito.anyString());

        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals("Description1", actual.get(0).getDescription());
        Assertions.assertEquals("AFF", actual.get(0).getType());
        Assertions.assertEquals(BigDecimal.valueOf(7), actual.get(0).getStatutoryFeeAmount());
        Assertions.assertTrue(actual.get(0).isOrderDocument());
        Assertions.assertTrue(actual.get(0).isRushRequired());
        Assertions.assertFalse(actual.get(0).isAutoProcessing());

    }
}
