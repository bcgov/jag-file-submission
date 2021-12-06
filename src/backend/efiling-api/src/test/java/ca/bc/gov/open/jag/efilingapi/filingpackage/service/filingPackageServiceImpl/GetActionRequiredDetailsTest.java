package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.ActionDocument;
import ca.bc.gov.open.jag.efilingapi.api.model.ActionRequiredDetails;
import ca.bc.gov.open.jag.efilingapi.error.NoRegistryNoticeException;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.ActionRequiredDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static ca.bc.gov.open.jag.efilingapi.TestHelpers.DOCUMENT_ID_TWO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetActionRequiredDetailsTest {
    public static final String EXPECTED_ISO = "2020-05-05T00:00:00.000-07:00";

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

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, null, accountServiceMock, new FilingPackageMapperImpl(), new ActionRequiredDetailsMapperImpl(), null);
    }

    @Test
    @DisplayName("Ok: action details returned was returned")
    public void withValidRequestReturnActionRequiredDetails() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage(true)));

        Optional<ActionRequiredDetails> result = sut.getActionRequiredDetails(TestHelpers.CASE_1_STRING, BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());

        Assertions.assertEquals(BigDecimal.ONE, result.get().getClientId());
        Assertions.assertEquals(BigDecimal.valueOf(123), result.get().getPackageIdentifier());
        //Assert Documents
        Assertions.assertEquals(1, result.get().getDocuments().size());
        Assertions.assertEquals(new BigDecimal(DOCUMENT_ID_TWO), result.get().getDocuments().get(0).getId());
        Assertions.assertEquals(ActionDocument.StatusEnum.REJ, result.get().getDocuments().get(0).getStatus());
        Assertions.assertEquals("AAB", result.get().getDocuments().get(0).getType());
    }

    @Test
    @DisplayName("Not found: missing account")
    public void withValidRequestButMissingAccountReturnEmpty() {

        Optional<ActionRequiredDetails> result = sut.getActionRequiredDetails(TestHelpers.CASE_2_STRING, BigDecimal.ONE);

        Assertions.assertFalse(result.isPresent());

    }


    @Test
    @DisplayName("Not found: no filing package")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Optional<ActionRequiredDetails> result = sut.getActionRequiredDetails(TestHelpers.CASE_1_STRING, BigDecimal.TEN);

        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @DisplayName("No Registry Notice: throws exception")
    public void withValidRequestButNoRegistryNoticeEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage(false)));

        Assertions.assertThrows(NoRegistryNoticeException.class, () -> sut.getActionRequiredDetails(TestHelpers.CASE_1_STRING, BigDecimal.TEN));

    }

}

