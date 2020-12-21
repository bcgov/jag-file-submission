package ca.bc.gov.open.jag.packagereview;

import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilingPackageMapperImpl;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;


//@QuarkusTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewResourceTest {

    private static final BigDecimal SUCCESS_PACKAGE = BigDecimal.ONE;
    private static final BigDecimal SUCCESS_CLIENT = BigDecimal.ONE;
   // @Mock
    EfilingStatusService efilingStatusServiceMock;

    private ReviewResource sut;

 //   @BeforeAll
    public void beforeAll() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(efilingStatusServiceMock.findStatusByPackage(ArgumentMatchers.eq(SUCCESS_CLIENT),ArgumentMatchers.eq(SUCCESS_PACKAGE))).thenReturn(Optional.of(FilingPackage.builder().applicationCode("test").create()));

        //sut = new ReviewResource(efilingStatusServiceMock, new FilingPackageMapperImpl());
    }

   // @Test
    public void testEndpoint() {
        ca.bc.gov.open.jag.packagereview.model.FilingPackage result = sut.getSubmission(SUCCESS_CLIENT, SUCCESS_PACKAGE);
        Assertions.assertEquals(SUCCESS_CLIENT.toPlainString(), result.getClientFileNo());
        Assertions.assertEquals(SUCCESS_PACKAGE.toPlainString(), result.getPackageNo());
    }

}
