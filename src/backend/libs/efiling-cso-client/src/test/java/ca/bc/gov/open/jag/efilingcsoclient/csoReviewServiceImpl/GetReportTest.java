package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportsTypes;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GetPaymentReceiptTest")
public class GetReportTest {
    public static final String FILE_NAME = "TEST";
    @Mock
    ReportService reportServiceMock;

    @Mock
    private RestTemplate restTemplateMock;

    private static final byte[] SUCCESS = "TEST".getBytes();

    private static CsoReviewServiceImpl sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        CsoProperties csoProperties = new CsoProperties();
        csoProperties.setCsoBasePath("http://locahost:8080");

        sut = new CsoReviewServiceImpl(null, reportServiceMock, null, new FilePackageMapperImpl(), csoProperties, restTemplateMock, null);
    }

    @DisplayName("OK: submission sheet return")
    @Test
    public void testWithSubmissionSheetFoundResult() {

        Mockito.when(reportServiceMock.runReport(any())).thenReturn(SUCCESS);

        Optional<byte[]> result = sut.getReport(ReportRequest.builder().fileName(FILE_NAME).packageId(BigDecimal.ONE).report(ReportsTypes.SUBMISSION_SHEET).create());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(SUCCESS, result.get());

    }

    @DisplayName("OK: receipt return")
    @Test
    public void testWithReceiptFoundResult() {

        Mockito.when(reportServiceMock.runReport(any())).thenReturn(SUCCESS);

        Optional<byte[]> result = sut.getReport(ReportRequest.builder().fileName(FILE_NAME).packageId(BigDecimal.ONE).report(ReportsTypes.PAYMENT_RECEIPT).create());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(SUCCESS, result.get());

    }

    @DisplayName("OK: registry notice return")
    @Test
    public void testWithRegistryNoticeResult() {

        Mockito.when(reportServiceMock.runReport(any())).thenReturn(SUCCESS);

        Optional<byte[]> result = sut.getReport(ReportRequest.builder().fileName(FILE_NAME).packageId(BigDecimal.ONE).report(ReportsTypes.REGISTRY_NOTICE).create());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(SUCCESS, result.get());

    }

    @DisplayName("OK: null result ")
    @Test
    public void testWithNullResult() {
        Mockito.when(reportServiceMock.runReport(any())).thenReturn(null);

        Optional<byte[]> result = sut.getReport(ReportRequest.builder().fileName(FILE_NAME).packageId(BigDecimal.ONE).report(ReportsTypes.PAYMENT_RECEIPT).create());

        Assertions.assertFalse(result.isPresent());
    }

    @DisplayName("OK: empty ")
    @Test
    public void testWithExceptionResult() {
        Mockito.when(reportServiceMock.runReport(any())).thenReturn(new byte[0]);

        Optional<byte[]> result = sut.getReport(ReportRequest.builder().fileName(FILE_NAME).packageId(BigDecimal.ONE).report(ReportsTypes.PAYMENT_RECEIPT).create());

        Assertions.assertFalse(result.isPresent());

    }
}
