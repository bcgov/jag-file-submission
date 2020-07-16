package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.fee.FeeService;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Submission Model test suite")
public class SubmissionTest {


    private static final String CASE_1 = "CASE1";
    private static final String CANCEL = "CANCEL";
    private static final String ERROR = "ERROR";
    private static final String TYPE = "TYPE";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String EMAIL = "email";
    private static final String DISPLAYNAME = "DISPLAYNAME";

    @Test
    @DisplayName("CASE 1: testing constructor")
    public void testingConstructor() {
        ServiceFees fee = new ServiceFees(null, BigDecimal.TEN, null, "DCFL",null, null, null, null);
        AccountDetails accountDetails = new AccountDetails(UUID.randomUUID(), BigDecimal.TEN, BigDecimal.ONE, true, FIRST_NAME, LAST_NAME, MIDDLE_NAME, EMAIL);


        Submission actual = new Submission(
                UUID.randomUUID(),
                TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()),
                TestHelpers.createNavigation(CASE_1, CANCEL, ERROR),
                TestHelpers.createClientApplication(DISPLAYNAME, TYPE),
                Arrays.asList(fee,fee),
                accountDetails,
                1);


        Assertions.assertEquals(TYPE, actual.getClientApplication().getType());
        Assertions.assertEquals(DISPLAYNAME, actual.getClientApplication().getDisplayName());
        Assertions.assertEquals(ERROR, actual.getNavigation().getError().getUrl());
        Assertions.assertEquals(CANCEL, actual.getNavigation().getCancel().getUrl());
        Assertions.assertEquals(CASE_1, actual.getNavigation().getSuccess().getUrl());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFees().get(0).getFeeAmt());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFees().get(1).getFeeAmt());
        Assertions.assertNotNull(actual.getAccountDetails().getUniversalId());
        Assertions.assertEquals(EMAIL, actual.getAccountDetails().getEmail());
        Assertions.assertEquals(FIRST_NAME, actual.getAccountDetails().getFirstName());
        Assertions.assertEquals(LAST_NAME, actual.getAccountDetails().getLastName());
        Assertions.assertEquals(MIDDLE_NAME, actual.getAccountDetails().getMiddleName());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountDetails().getAccountId());
        Assertions.assertEquals(BigDecimal.ONE, actual.getAccountDetails().getClientId());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getModelPackage().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getModelPackage().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getModelPackage().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getModelPackage().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getModelPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getModelPackage().getCourt().getPropertyClass());
        Assertions.assertEquals(TestHelpers.TYPE, actual.getModelPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getModelPackage().getDocuments().get(0).getDescription());
    }

}
