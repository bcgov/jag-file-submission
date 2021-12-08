package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.ParentAppDetails;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.properties.ParentAppProperties;
import ca.bc.gov.open.jag.efilingapi.filingpackage.properties.ParentProperties;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetParentAppDetailsTestTest {

    private static final String RETURN_URL = "http://localhost:8080";
    FilingPackageServiceImpl sut;

    @Mock
    EfilingReviewService efilingReviewServiceMock;

    @Mock
    AccountService accountServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING))).thenReturn(TestHelpers.createAccount(BigDecimal.ONE));

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING))).thenReturn(TestHelpers.createAccount(null));

        ParentAppProperties parentAppProperties = new ParentAppProperties();
        parentAppProperties.setApplication(TestHelpers.APPLICATION_CODE);
        parentAppProperties.setRejectedDocuments(true);
        parentAppProperties.setReturnUrl(RETURN_URL);

        ParentProperties parentProperties = new ParentProperties();

        parentProperties.setParents(Arrays.asList(parentAppProperties));

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, null, accountServiceMock, new FilingPackageMapperImpl(), null, parentProperties);
    }


    @Test
    @DisplayName("Ok: parent app details returned")
    public void withValidRequestReturnDocument() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage(true)));

        Optional<ParentAppDetails> result = sut.getParentDetails(TestHelpers.CASE_1_STRING, BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(RETURN_URL, result.get().getReturnUrl());
        Assertions.assertTrue(result.get().getRejectedDocumentFeature());

    }

    @Test
    @DisplayName("Not found: missing parent ")
    public void withValidRequestButMissingParentReturnEmpty() {

        ReviewFilingPackage reviewFilingPackage = TestHelpers.createFilingPackage(true);

        reviewFilingPackage.setApplicationCode("MISSING");

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(reviewFilingPackage));

        Optional<ParentAppDetails> result = sut.getParentDetails(TestHelpers.CASE_1_STRING, BigDecimal.ONE);
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @DisplayName("Not found: missing account")
    public void withValidRequestButMissingAccountReturnEmpty() {

        Optional<ParentAppDetails> result = sut.getParentDetails(TestHelpers.CASE_2_STRING, BigDecimal.ONE);
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @DisplayName("Not found: no filing package")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Optional<ParentAppDetails> result = sut.getParentDetails(TestHelpers.CASE_1_STRING, BigDecimal.TEN);

        Assertions.assertFalse(result.isPresent());

    }

}
