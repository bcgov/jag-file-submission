package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.filing.status.FilingStatus;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Review Service Test Suite")
public class GetSubmissionSheetTest {
    @Mock
    ReportService reportServiceMock;

    private static CsoReviewServiceImpl sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new CsoReviewServiceImpl(null, reportServiceMock, new FilePackageMapperImpl());
    }

    @DisplayName("OK: report return")
    @Test
    public void testWithFoundResult() {

    }

    @DisplayName("OK: null result ")
    @Test
    public void testWithNullResult() {

    }

    @DisplayName("Exception: throws exception ")
    @Test
    public void testWithExceptionResult() {

    }
}
