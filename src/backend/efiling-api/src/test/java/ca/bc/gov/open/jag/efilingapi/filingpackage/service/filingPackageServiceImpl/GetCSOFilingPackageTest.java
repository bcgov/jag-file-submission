package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Rush;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetCSOFilingPackageTest {

    public static final String EXPECTED_ISO = "2020-05-05T00:00:00.000-07:00";

    FilingPackageServiceImpl sut;

    @Mock
    EfilingReviewService efilingReviewServiceMock;


    @Mock
    EfilingDocumentService efilingDocumentServiceMock;

    @Mock
    AccountService accountServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING))).thenReturn(TestHelpers.createAccount(BigDecimal.ONE));

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING))).thenReturn(TestHelpers.createAccount(null));

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, efilingDocumentServiceMock, accountServiceMock, new FilingPackageMapperImpl(), null, null);
    }

    @Test
    @DisplayName("Ok: a filing package was returned")
    public void withValidRequestReturnFilingPackage() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage(true)));

        Mockito.when(efilingDocumentServiceMock.getDocumentTypeDetails(any(), any(), any(), any())).thenReturn(new DocumentTypeDetails("description",
                "type",
                BigDecimal.TEN,
                true,
                true,
                true));

        Optional<FilingPackage> result = sut.getCSOFilingPackage(TestHelpers.CASE_1_STRING, BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());
        //FilingPackage
        Assertions.assertEquals(TestHelpers.COMMENT, result.get().getFilingComments());
        Assertions.assertEquals(new BigDecimal(TestHelpers.PACKAGE_NO), result.get().getPackageNumber());
        Assertions.assertNotNull(result.get().getSubmittedDate());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, result.get().getSubmittedBy().getFirstName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, result.get().getSubmittedBy().getLastName());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getSubmittedDate());

        //Court
        Assertions.assertEquals(TestHelpers.CLASS_DESCRIPTION, result.get().getCourt().getClassDescription());
        Assertions.assertEquals(TestHelpers.COURT_CLASS, result.get().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.DIVISION, result.get().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILE_NUMBER, result.get().getCourt().getFileNumber());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getCourt().getAgencyId());
        Assertions.assertEquals(TestHelpers.LOCATION, result.get().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.LEVEL, result.get().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LEVEL_DESCRIPTION, result.get().getCourt().getLevelDescription());
        Assertions.assertEquals(TestHelpers.LOCATION_DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(TestHelpers.PARTICIPATING_CLASS, result.get().getCourt().getParticipatingClass());
        //Party
        Assertions.assertEquals(1, result.get().getParties().size());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_DESC, result.get().getParties().get(0).getPartyDescription());
        Assertions.assertEquals(ca.bc.gov.open.jag.efilingapi.api.model.Individual.RoleTypeEnum.ABC, result.get().getParties().get(0).getRoleType());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_DESC, result.get().getParties().get(0).getRoleDescription());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, result.get().getParties().get(0).getFirstName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, result.get().getParties().get(0).getLastName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, result.get().getParties().get(0).getMiddleName());
        //Document
        Assertions.assertEquals(2, result.get().getDocuments().size());
        Assertions.assertEquals("AAB", result.get().getDocuments().get(0).getDocumentProperties().getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, result.get().getDocuments().get(0).getDescription());
        Assertions.assertEquals(TestHelpers.NAME, result.get().getDocuments().get(0).getDocumentProperties().getName());
        Assertions.assertEquals(TestHelpers.STATUS, result.get().getDocuments().get(0).getStatus().getDescription());
        Assertions.assertEquals(TestHelpers.STATUS_CODE, result.get().getDocuments().get(0).getStatus().getCode());
        Assertions.assertTrue( result.get().getDocuments().get(0).getRushRequired());
        Assertions.assertNotNull(result.get().getDocuments().get(0).getStatus().getChangeDate());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getDocuments().get(0).getStatus().getChangeDate());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getDocuments().get(0).getFilingDate());
        //Payments
        Assertions.assertEquals(1, result.get().getPayments().size());
        Assertions.assertEquals(false, result.get().getPayments().get(0).getFeeExempt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getProcessedAmount());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getProcessedAmount());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getServiceIdentifier());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(TestHelpers.TRANSACTION_DESC, result.get().getPayments().get(0).getPaymentDescription());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getPayments().get(0).getTransactionDate());
        //Rush
        Assertions.assertEquals(Rush.RushTypeEnum.OTHER, result.get().getRush().getRushType());
        Assertions.assertEquals(TestHelpers.CONTACT_EMAIL_TXT, result.get().getRush().getEmail());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, result.get().getRush().getFirstName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, result.get().getRush().getLastName());
        Assertions.assertEquals(TestHelpers.ORGANIZATION_NM, result.get().getRush().getOrganization());
        Assertions.assertEquals(TestHelpers.CONTACT_PHONE_NO, result.get().getRush().getPhoneNumber());
        Assertions.assertEquals(TestHelpers.CURRENT_STATUS_DSC, result.get().getRush().getStatus());
        Assertions.assertEquals(BigDecimal.ONE.toEngineeringString(), result.get().getRush().getCountryCode());
        Assertions.assertEquals(TestHelpers.COUNTRY_DSC, result.get().getRush().getCountry());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getRush().getCourtDate());
        Assertions.assertEquals(TestHelpers.NOTICE_REASON_TEXT, result.get().getRush().getStatusReason());
        Assertions.assertEquals(TestHelpers.RUSH_FILING_REASON_TXT, result.get().getRush().getReason());

        //Rush Documents
        Assertions.assertEquals(2, result.get().getRush().getSupportingDocuments().size());

        Assertions.assertEquals(TestHelpers.CLIENT_FILE_NM, result.get().getRush().getSupportingDocuments().get(0).getFileName());
        Assertions.assertEquals(TestHelpers.OBJECT_GUID, result.get().getRush().getSupportingDocuments().get(0).getIdentifier());

        Assertions.assertEquals(TestHelpers.CLIENT_FILE_NM, result.get().getRush().getSupportingDocuments().get(1).getFileName());
        Assertions.assertEquals(TestHelpers.OBJECT_GUID, result.get().getRush().getSupportingDocuments().get(1).getIdentifier());

        Assertions.assertEquals("http://localhost:8080/showmustgoon", result.get().getLinks().getPackageHistoryUrl());

    }

    @Test
    @DisplayName("Ok: a filing package was returned ensure withdrawn included")
    public void withValidRequestReturnFilingPackageWithWithdrawnExcluded() {

        ReviewFilingPackage reviewFilingPackage = TestHelpers.createFilingPackage(true);

        ReviewDocument withdrawnDocument = new ReviewDocument();
        withdrawnDocument.setDocumentId("TEST");
        withdrawnDocument.setFileName("TEST");
        withdrawnDocument.setDocumentTypeCd("AAB");
        withdrawnDocument.setDocumentType("TEST");
        withdrawnDocument.setStatus("TEST");
        withdrawnDocument.setStatusCode(ca.bc.gov.open.jag.efilingcommons.Keys.WITHDRAWN_STATUS_CD);
        withdrawnDocument.setStatusDate(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        withdrawnDocument.setPaymentProcessed(true);

        reviewFilingPackage.getDocuments().add(withdrawnDocument);

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(reviewFilingPackage));

        Optional<FilingPackage> result = sut.getCSOFilingPackage(TestHelpers.CASE_1_STRING, BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());
        //FilingPackage
        //Document
        Assertions.assertEquals(3, result.get().getDocuments().size());

    }


    @Test
    @DisplayName("Not found: missing account")
    public void withValidRequestButMissingAccountReturnEmpty() {
        Optional<FilingPackage> result = sut.getCSOFilingPackage(TestHelpers.CASE_2_STRING, BigDecimal.ONE);

        Assertions.assertFalse(result.isPresent());
    }


    @Test
    @DisplayName("Not found: no filing package")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Optional<FilingPackage> result = sut.getCSOFilingPackage(TestHelpers.CASE_1_STRING, BigDecimal.TEN);

        Assertions.assertFalse(result.isPresent());
    }

}
