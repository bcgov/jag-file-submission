package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialDocument;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.PackagePayment;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewCourt;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetCSOFilingPackage {

    public static final String CLASS_DESCRIPTION = "CLASS_DESCRIPTION";
    public static final String COURT_CLASS = "COURT_CLASS";
    public static final String DIVISION = "DIVISION";
    public static final String FILE_NUMBER = "FILE_NUMBER";
    public static final String LEVEL = "LEVEL";
    public static final String LEVEL_DESCRIPTION = "LEVEL_DESCRIPTION";
    public static final String LOCATION = "LOCATION";
    public static final String LOCATION_DESCRIPTION = "LOCATION_DESCRIPTION";
    public static final String PARTICIPATING_CLASS = "PARTICIPATING_CLASS";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String MIDDLE_NAME = "MIDDLENAME";
    public static final String NAME_TYPE = "NAME_TYPE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final String STATUS = "STATUS";
    public static final String STATUS_CODE = "STATUSCODE";
    public static final String COMMENT = "COMMENT";
    public static final String PACKAGE_NO = "123";
    public static final String EXPECTED_ISO = "2020-05-05T00:00:00.000-07:00";
    FilingPackageServiceImpl sut;

    @Mock
    EfilingReviewService efilingReviewServiceMock;

    @Mock
    AccountService accountServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING))).thenReturn(createAccount(BigDecimal.ONE));

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING))).thenReturn(createAccount(null));

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, accountServiceMock, new FilingPackageMapperImpl());
    }

    @Test
    @DisplayName("Ok: a filing package was returned")
    public void withValidRequestReturnFilingPackage() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(createFilingPackage()));

        Optional<FilingPackage> result = sut.getCSOFilingPackage(TestHelpers.CASE_1_STRING, BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());
        //FilingPackage
        Assertions.assertEquals(COMMENT, result.get().getFilingComments());
        Assertions.assertEquals(new BigDecimal(PACKAGE_NO), result.get().getPackageNumber());
        Assertions.assertNotNull(result.get().getSubmittedDate());
        Assertions.assertEquals(FIRST_NAME, result.get().getSubmittedBy().getFirstName());
        Assertions.assertEquals(LAST_NAME, result.get().getSubmittedBy().getLastName());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getSubmittedDate());

        //Court
        Assertions.assertEquals(CLASS_DESCRIPTION, result.get().getCourt().getClassDescription());
        Assertions.assertEquals(COURT_CLASS, result.get().getCourt().getCourtClass());
        Assertions.assertEquals(DIVISION, result.get().getCourt().getDivision());
        Assertions.assertEquals(FILE_NUMBER, result.get().getCourt().getFileNumber());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getCourt().getAgencyId());
        Assertions.assertEquals(LOCATION, result.get().getCourt().getLocation());
        Assertions.assertEquals(LEVEL, result.get().getCourt().getLevel());
        Assertions.assertEquals(LEVEL_DESCRIPTION, result.get().getCourt().getLevelDescription());
        Assertions.assertEquals(LOCATION_DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(PARTICIPATING_CLASS, result.get().getCourt().getParticipatingClass());
        //Party
        Assertions.assertEquals(1, result.get().getParties().size());
        Assertions.assertEquals(ca.bc.gov.open.jag.efilingapi.api.model.Party.PartyTypeEnum.IND, result.get().getParties().get(0).getPartyType());
        Assertions.assertEquals(ca.bc.gov.open.jag.efilingapi.api.model.Party.RoleTypeEnum.ABC, result.get().getParties().get(0).getRoleType());
        Assertions.assertEquals(FIRST_NAME, result.get().getParties().get(0).getFirstName());
        Assertions.assertEquals(LAST_NAME, result.get().getParties().get(0).getLastName());
        Assertions.assertEquals(MIDDLE_NAME, result.get().getParties().get(0).getMiddleName());
        //Document
        Assertions.assertEquals(1, result.get().getDocuments().size());
        Assertions.assertEquals(DocumentProperties.TypeEnum.AAB, result.get().getDocuments().get(0).getDocumentProperties().getType());
        Assertions.assertEquals(NAME, result.get().getDocuments().get(0).getDocumentProperties().getName());
        Assertions.assertEquals(STATUS, result.get().getDocuments().get(0).getStatus().getDescription());
        Assertions.assertEquals(STATUS_CODE, result.get().getDocuments().get(0).getStatus().getCode());
        Assertions.assertNotNull(result.get().getDocuments().get(0).getStatus().getChangeDate());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getDocuments().get(0).getStatus().getChangeDate());
        //Payments
        Assertions.assertEquals(1, result.get().getPayments().size());
        Assertions.assertEquals(false, result.get().getPayments().get(0).getFeeExempt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getProcessedAmount());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getProcessedAmount());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getServiceIdentifier());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(EXPECTED_ISO, result.get().getPayments().get(0).getTransactionDate());
        Assertions.assertNotNull(result.get().getPayments().get(0).getTransactionDate());

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

    private AccountDetails createAccount(BigDecimal clientId) {

        return AccountDetails.builder()
                .fileRolePresent(true)
                .accountId(BigDecimal.ONE)
                .clientId(clientId)
                .cardRegistered(true)
                .universalId(UUID.randomUUID().toString())
                .internalClientNumber(null)
                .universalId(TestHelpers.CASE_1_STRING).create();

    }

    private ReviewFilingPackage createFilingPackage() {

        ReviewFilingPackage reviewFilingPackage = new ReviewFilingPackage();
        reviewFilingPackage.setClientFileNo("CLIENTFILENO");
        reviewFilingPackage.setFilingCommentsTxt(COMMENT);
        reviewFilingPackage.setPackageNo(PACKAGE_NO);
        reviewFilingPackage.setFirstName(FIRST_NAME);
        reviewFilingPackage.setLastName(LAST_NAME);
        reviewFilingPackage.setSubmittedDate(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewFilingPackage.setCourt(createCourt());
        reviewFilingPackage.setDocuments(Collections.singletonList(createDocument()));
        reviewFilingPackage.setParties(Collections.singletonList(createParty()));
        reviewFilingPackage.setPayments(Collections.singletonList(createPayment()));
        return reviewFilingPackage;

    }

    private ReviewCourt createCourt() {

        ReviewCourt reviewCourt = new ReviewCourt();
        reviewCourt.setClassDescription(CLASS_DESCRIPTION);
        reviewCourt.setCourtClass(COURT_CLASS);
        reviewCourt.setDivision(DIVISION);
        reviewCourt.setFileNumber(FILE_NUMBER);
        reviewCourt.setLevel(LEVEL);
        reviewCourt.setLevelDescription(LEVEL_DESCRIPTION);
        reviewCourt.setLocationId(BigDecimal.ONE);
        reviewCourt.setLocationName(LOCATION);
        reviewCourt.setLocationDescription(LOCATION_DESCRIPTION);
        reviewCourt.setParticipatingClass(PARTICIPATING_CLASS);
        return reviewCourt;

    }

    private Party createParty() {
        return Party.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .middleName(MIDDLE_NAME)
                .nameTypeCd(NAME_TYPE)
                .partyTypeCd(ca.bc.gov.open.jag.efilingapi.api.model.Party.PartyTypeEnum.IND.getValue())
                .roleTypeCd(ca.bc.gov.open.jag.efilingapi.api.model.Party.RoleTypeEnum.ABC.getValue())
                .create();
    }

    private ReviewDocument createDocument() {

        ReviewDocument reviewDocument = new ReviewDocument();
        reviewDocument.setFileName(NAME);
        reviewDocument.setDocumentTypeCd(DocumentProperties.TypeEnum.AAB.getValue());
        reviewDocument.setStatus(STATUS);
        reviewDocument.setStatusCode(STATUS_CODE);
        reviewDocument.setStatusDate(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        reviewDocument.setPaymentProcessed(true);
        return reviewDocument;

    }

    private PackagePayment createPayment() {

        PackagePayment packagePayment = new PackagePayment();
        packagePayment.setFeeExmpt(false);
        packagePayment.setPaymentCategory(BigDecimal.ONE);
        packagePayment.setProcessedAmt(BigDecimal.ONE);
        packagePayment.setSubmittedAmt(BigDecimal.ONE);
        packagePayment.setServiceId(BigDecimal.ONE);
        packagePayment.setTransactionDtm(DateTime.parse("2020-05-05T00:00:00.000-07:00"));
        return packagePayment;

    }
}
