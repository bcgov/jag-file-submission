package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class GetPaymentReceiptTest {
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

    @DisplayName("OK: receipt return")
    @Test
    public void testWithFoundResult() {

        Optional<byte[]> result = sut.getPaymentReceipt(BigDecimal.ONE);

        Assertions.assertFalse(result.isPresent());

    }

}
