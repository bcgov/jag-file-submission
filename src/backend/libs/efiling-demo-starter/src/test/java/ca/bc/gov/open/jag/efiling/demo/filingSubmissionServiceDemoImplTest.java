package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Submission Client Test Suite")
public class filingSubmissionServiceDemoImplTest {
    @DisplayName("CASE 1: Testing Demo submitFilingPackage")
    @Test
    public void testDemoLookupFilingPackageServiceTest() {

        EfilingSubmissionServiceDemoImpl service = new EfilingSubmissionServiceDemoImpl();

        BigDecimal actual = service.submitFilingPackage(UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe"));

        Assertions.assertEquals(BigDecimal.ONE, actual);

    }
    @DisplayName("CASE 2: Testing Demo addService")
    @Test
    public void testDemoLookupAddServiceTest() {

        EfilingSubmissionServiceDemoImpl service = new EfilingSubmissionServiceDemoImpl();

        EfilingService actual = service.addService(new EfilingService());

        Assertions.assertEquals(BigDecimal.TEN, actual.getServiceId());
        Assertions.assertEquals(DateTime.now().getYear(), actual.getServiceReceivedDateTime().getYear());
        Assertions.assertEquals("Some recieved date message?", actual.getServiceReceivedDtmText());
        Assertions.assertEquals(BigDecimal.TEN, actual.getServiceSessionId());
        Assertions.assertEquals("subTypeCd", actual.getServiceSubtypeCd());
        Assertions.assertEquals("DCFL", actual.getServiceTypeCd());
        Assertions.assertEquals("Service type description", actual.getServiceTypeDesc());

    }
}
