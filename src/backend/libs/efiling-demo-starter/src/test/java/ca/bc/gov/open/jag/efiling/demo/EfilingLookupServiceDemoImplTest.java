package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFeeRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Lookup Client Test Suite")
public class EfilingLookupServiceDemoImplTest {

    @DisplayName("CASE 1: Testing Demo getServiceFee")
    @Test
    public void testDemoLookupServiceTest() {

        EfilingLookupServiceDemoImpl service = new EfilingLookupServiceDemoImpl();

        ServiceFees actual = service.getServiceFee(SubmissionFeeRequest.builder().create());

        Assertions.assertEquals(BigDecimal.valueOf(7), actual.getFeeAmount());
        Assertions.assertEquals("serviceTypeCd", actual.getServiceTypeCd());

    }

    @DisplayName("CASE 2: Testing Demo getCountries")
    @Test
    public void testDemoLookupServiceCountriesTest() {

        EfilingLookupServiceDemoImpl service = new EfilingLookupServiceDemoImpl();

        List<LookupItem> actual = service.getCountries();

        Assertions.assertEquals(3, actual.size());

        Assertions.assertEquals("1", actual.get(0).getCode());
        Assertions.assertEquals("Canada", actual.get(0).getDescription());

        Assertions.assertEquals("1", actual.get(1).getCode());
        Assertions.assertEquals("United States", actual.get(1).getDescription());

        Assertions.assertEquals("34", actual.get(2).getCode());
        Assertions.assertEquals("Spain", actual.get(2).getDescription());

    }

}
