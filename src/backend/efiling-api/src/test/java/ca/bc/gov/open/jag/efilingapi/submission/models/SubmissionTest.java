package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.api.model.Court;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
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
    private static final String DIVISION = "DIVISION";
    private static final String FILENUMBER = "FILENUMBER";
    private static final String LEVEL = "LEVEL";
    private static final String LOCATION = "LOCATION";
    private static final String PARTICIPATIONCLASS = "PARTICIPATIONCLASS";
    private static final String PROPERTYCLASS = "PROPERTYCLASS";
    private static final String DESCRIPTION = "DESCRIPTION";

    @Test
    @DisplayName("CASE 1: testing constructor")
    public void testingConstructor() {
        Fee fee = new Fee(BigDecimal.TEN);
        AccountDetails accountDetails = new AccountDetails(UUID.randomUUID(), BigDecimal.TEN, BigDecimal.ONE, true, FIRST_NAME, LAST_NAME, MIDDLE_NAME, EMAIL);
        Court court = new Court();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setPropertyClass(PROPERTYCLASS);

        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setType(TYPE);
        documentProperties.setDescription(DESCRIPTION);

        Submission actual = new Submission(
                UUID.randomUUID(),
                TestHelpers.createPackage(court, Arrays.asList(documentProperties)),
                TestHelpers.createNavigation(CASE_1, CANCEL, ERROR),
                TestHelpers.createClientApplication(DISPLAYNAME, TYPE),
                fee,
                accountDetails,
                1);


        Assertions.assertEquals(TYPE, actual.getClientApplication().getType());
        Assertions.assertEquals(DISPLAYNAME, actual.getClientApplication().getDisplayName());
        Assertions.assertEquals(ERROR, actual.getNavigation().getError().getUrl());
        Assertions.assertEquals(CANCEL, actual.getNavigation().getCancel().getUrl());
        Assertions.assertEquals(CASE_1, actual.getNavigation().getSuccess().getUrl());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFee().getAmount());
        Assertions.assertNotNull(actual.getAccountDetails().getUniversalId());
        Assertions.assertEquals(EMAIL, actual.getAccountDetails().getEmail());
        Assertions.assertEquals(FIRST_NAME, actual.getAccountDetails().getFirstName());
        Assertions.assertEquals(LAST_NAME, actual.getAccountDetails().getLastName());
        Assertions.assertEquals(MIDDLE_NAME, actual.getAccountDetails().getMiddleName());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountDetails().getAccountId());
        Assertions.assertEquals(BigDecimal.ONE, actual.getAccountDetails().getClientId());
        Assertions.assertEquals(DIVISION, actual.getModelPackage().getCourt().getDivision());
        Assertions.assertEquals(FILENUMBER, actual.getModelPackage().getCourt().getFileNumber());
        Assertions.assertEquals(LEVEL, actual.getModelPackage().getCourt().getLevel());
        Assertions.assertEquals(LOCATION, actual.getModelPackage().getCourt().getLocation());
        Assertions.assertEquals(PARTICIPATIONCLASS, actual.getModelPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(PROPERTYCLASS, actual.getModelPackage().getCourt().getPropertyClass());
        Assertions.assertEquals(TYPE, actual.getModelPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(DESCRIPTION, actual.getModelPackage().getDocuments().get(0).getDescription());
    }

}
