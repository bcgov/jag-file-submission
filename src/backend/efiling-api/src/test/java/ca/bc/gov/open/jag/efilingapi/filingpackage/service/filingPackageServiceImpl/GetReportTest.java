package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetReportTest {
    public static final byte[] BYTES = "TEST".getBytes();
    FilingPackageServiceImpl sut;

    @Mock
    EfilingReviewService efilingReviewServiceMock;


    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, null, new FilingPackageMapperImpl());
    }

    @Test
    @DisplayName("Ok: a filing package was returned")
    public void withValidRequestReturnFilingPackage() {

        Mockito.when(efilingReviewServiceMock.getReport(ArgumentMatchers.any())).thenReturn(Optional.of(BYTES));

        Optional<Resource> result = sut.getReport(ReportRequest.builder().create());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(new ByteArrayResource(BYTES), result.get());

    }

    @Test
    @DisplayName("Not found: no filing package")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.getReport(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Optional<Resource> result = sut.getReport(ReportRequest.builder().create());

        Assertions.assertFalse(result.isPresent());
    }
}
