package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.reports.ReportService;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class GetSubmissionDocumentTest {

    @Mock
    ReportService reportServiceMock;

    private static CsoReviewServiceImpl sut;

    @BeforeEach
    public void beforeEach() {

        MockitoAnnotations.openMocks(this);

        sut = new CsoReviewServiceImpl(null, reportServiceMock, new FilePackageMapperImpl());
    }

    @DisplayName("OK: it is working")
    @Test
    public void testWithFoundResult() {

        Optional<byte[]> result = sut.getSubmittedDocument(BigDecimal.ONE, "");

        Assertions.assertFalse(result.isPresent());

    }

}
