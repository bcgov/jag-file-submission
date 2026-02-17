package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetFilingPackagesTest {

    public static final String EXPECTED_ISO = "2020-05-05T00:00:00.000-07:00";
    public static final String PARENT_APPLICATION = "PARENT_APPLICATION";
    public static final String PARENT_APPLICATION_NOT_FOUND = "PARENT_APPLICATION_NOT_FOUND";

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

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, null, accountServiceMock, new FilingPackageMapperImpl(), null, null);

    }

    @Test
    @DisplayName("Ok: a filing package was returned")
    public void withValidRequestReturnFilingPackage() {

        Mockito.when(efilingReviewServiceMock.findStatusByClient(ArgumentMatchers.any())).thenReturn(Collections.singletonList(TestHelpers.createFilingPackage(true)));

        Optional<List<FilingPackage>> actual = sut.getFilingPackages(TestHelpers.CASE_1_STRING, PARENT_APPLICATION);

        Assertions.assertTrue(actual.isPresent());
        //FilingPackage
        Assertions.assertEquals(TestHelpers.COMMENT, actual.get().get(0).getFilingComments());
        Assertions.assertEquals(new BigDecimal(TestHelpers.PACKAGE_NO), actual.get().get(0).getPackageNumber());
        Assertions.assertNotNull(actual.get().get(0).getSubmittedDate());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.get().get(0).getSubmittedBy().getFirstName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.get().get(0).getSubmittedBy().getLastName());
        Assertions.assertEquals(EXPECTED_ISO, actual.get().get(0).getSubmittedDate());

        //Court
        Assertions.assertEquals(TestHelpers.CLASS_DESCRIPTION, actual.get().get(0).getCourt().getClassDescription());
        Assertions.assertEquals(TestHelpers.COURT_CLASS, actual.get().get(0).getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.get().get(0).getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILE_NUMBER, actual.get().get(0).getCourt().getFileNumber());
        Assertions.assertEquals(BigDecimal.ONE, actual.get().get(0).getCourt().getAgencyId());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.get().get(0).getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.get().get(0).getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LEVEL_DESCRIPTION, actual.get().get(0).getCourt().getLevelDescription());
        Assertions.assertEquals(TestHelpers.LOCATION_DESCRIPTION, actual.get().get(0).getCourt().getLocationDescription());
        Assertions.assertEquals(TestHelpers.PARTICIPATING_CLASS, actual.get().get(0).getCourt().getParticipatingClass());
        //Party
        Assertions.assertEquals(1, actual.get().get(0).getParties().size());
        Assertions.assertEquals(TestHelpers.PARTY_TYPE_DESC, actual.get().get(0).getParties().get(0).getPartyDescription());
        Assertions.assertEquals(ca.bc.gov.open.jag.efilingapi.api.model.Individual.RoleTypeEnum.ABC, actual.get().get(0).getParties().get(0).getRoleType());
        Assertions.assertEquals(TestHelpers.ROLE_TYPE_DESC, actual.get().get(0).getParties().get(0).getRoleDescription());
        Assertions.assertEquals(TestHelpers.FIRST_NAME, actual.get().get(0).getParties().get(0).getFirstName());
        Assertions.assertEquals(TestHelpers.LAST_NAME, actual.get().get(0).getParties().get(0).getLastName());
        Assertions.assertEquals(TestHelpers.MIDDLE_NAME, actual.get().get(0).getParties().get(0).getMiddleName());
        //Document
        Assertions.assertEquals(2, actual.get().get(0).getDocuments().size());
        Assertions.assertEquals("AAB", actual.get().get(0).getDocuments().get(0).getDocumentProperties().getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.get().get(0).getDocuments().get(0).getDescription());
        Assertions.assertEquals(TestHelpers.NAME, actual.get().get(0).getDocuments().get(0).getDocumentProperties().getName());
        Assertions.assertEquals(TestHelpers.STATUS, actual.get().get(0).getDocuments().get(0).getStatus().getDescription());
        Assertions.assertEquals(TestHelpers.STATUS_CODE, actual.get().get(0).getDocuments().get(0).getStatus().getCode());
        Assertions.assertNotNull(actual.get().get(0).getDocuments().get(0).getStatus().getChangeDate());
        Assertions.assertEquals(EXPECTED_ISO, actual.get().get(0).getDocuments().get(0).getStatus().getChangeDate());
        Assertions.assertEquals(EXPECTED_ISO, actual.get().get(0).getDocuments().get(0).getFilingDate());
        //Payments
        Assertions.assertEquals(1, actual.get().get(0).getPayments().size());
        Assertions.assertEquals(false, actual.get().get(0).getPayments().get(0).getFeeExempt());
        Assertions.assertEquals(BigDecimal.ONE, actual.get().get(0).getPayments().get(0).getProcessedAmount());
        Assertions.assertEquals(BigDecimal.ONE, actual.get().get(0).getPayments().get(0).getProcessedAmount());
        Assertions.assertEquals(BigDecimal.ONE, actual.get().get(0).getPayments().get(0).getServiceIdentifier());
        Assertions.assertEquals(BigDecimal.ONE, actual.get().get(0).getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(TestHelpers.TRANSACTION_DESC, actual.get().get(0).getPayments().get(0).getPaymentDescription());
        Assertions.assertEquals(EXPECTED_ISO, actual.get().get(0).getPayments().get(0).getTransactionDate());

        Assertions.assertEquals("http://localhost:8080/showmustgoon", actual.get().get(0).getLinks().getPackageHistoryUrl());

    }

    @Test
    @DisplayName("Not found: missing account")
    public void withValidRequestButMissingAccountReturnEmpty() {
        Optional<List<FilingPackage>> actual = sut.getFilingPackages(TestHelpers.CASE_2_STRING, PARENT_APPLICATION_NOT_FOUND);

        Assertions.assertFalse(actual.isPresent());

    }

    @Test
    @DisplayName("Not found: no filing packages")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByClient(ArgumentMatchers.any())).thenReturn(new ArrayList<>());

        Optional<List<FilingPackage>> actual = sut.getFilingPackages(TestHelpers.CASE_1_STRING, PARENT_APPLICATION_NOT_FOUND);

        Assertions.assertFalse(actual.isPresent());

    }

}
