package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
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
@DisplayName("Review Service Test Suite")
public class GetSubmissionSheetTest {

    @Mock
    ReportService reportServiceMock;

    @Mock
    private RestTemplate restTemplateMock;

    private static final byte[] SUCCESS = "TEST".getBytes();

    private static CsoReviewServiceImpl sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new CsoReviewServiceImpl(null, reportServiceMock, null, new FilePackageMapperImpl(), restTemplateMock);
    }

    @DisplayName("OK: report return")
    @Test
    public void testWithFoundResult() {
        Mockito.when(reportServiceMock.runReport(any())).thenReturn(SUCCESS);

        Optional<byte[]> result = sut.getSubmissionSheet(BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(SUCCESS, result.get());

    }

    @DisplayName("OK: null result ")
    @Test
    public void testWithNullResult() {
        Mockito.when(reportServiceMock.runReport(any())).thenReturn(null);

        Optional<byte[]> result = sut.getSubmissionSheet(BigDecimal.ONE);

        Assertions.assertFalse(result.isPresent());
    }

    @DisplayName("OK: empty ")
    @Test
    public void testWithExceptionResult() {
        Mockito.when(reportServiceMock.runReport(any())).thenReturn(new byte[0]);

        Optional<byte[]> result = sut.getSubmissionSheet(BigDecimal.ONE);

        Assertions.assertFalse(result.isPresent());
    }
}
