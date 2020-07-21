package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Lookup Client Test Suite")
public class EfilingPackageServiceDemoImplTest {

    @DisplayName("CASE 1: Testing Demo getServiceFee")
    @Test
    public void testDemoLookupServiceTest() {

        EfilingPackageServiceDemoImpl service = new EfilingPackageServiceDemoImpl();

        String serviceId = "TestServiceId";
        SubmissionFee actual = service.getSubmissionFee(serviceId);

        Assertions.assertEquals(BigDecimal.valueOf(7), actual.getFeeAmount());
        Assertions.assertEquals("serviceTypeCd", actual.getServiceTypeCd());

    }

}
