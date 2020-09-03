package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
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
    private static final String INTERNAL_CLIENT_NUMBER = "INTERNALCLIENT";

    @Test
    @DisplayName("CASE 1: testing constructor")
    public void testingConstructor() {

        Submission actual = new Submission(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                AccountDetails.builder()
                        .clientId(BigDecimal.TEN)
                        .accountId(BigDecimal.TEN)
                        .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                        .create(),
                TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList(), TestHelpers.createPartyList()),
                TestHelpers.createNavigation(CASE_1, CANCEL, ERROR),
                TestHelpers.createClientApplication(DISPLAYNAME, TYPE),
                1, true);


        Assertions.assertEquals(TYPE, actual.getClientApplication().getType());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountDetails().getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountDetails().getClientId());
        Assertions.assertEquals(INTERNAL_CLIENT_NUMBER, actual.getAccountDetails().getInternalClientNumber());
        Assertions.assertEquals(DISPLAYNAME, actual.getClientApplication().getDisplayName());
        Assertions.assertEquals(ERROR, actual.getNavigationUrls().getError().getUrl());
        Assertions.assertEquals(CANCEL, actual.getNavigationUrls().getCancel().getUrl());
        Assertions.assertEquals(CASE_1, actual.getNavigationUrls().getSuccess().getUrl());
        Assertions.assertEquals(TestHelpers.DIVISION, actual.getFilingPackage().getCourt().getDivision());
        Assertions.assertEquals(TestHelpers.FILENUMBER, actual.getFilingPackage().getCourt().getFileNumber());
        Assertions.assertEquals(TestHelpers.LEVEL, actual.getFilingPackage().getCourt().getLevel());
        Assertions.assertEquals(TestHelpers.LOCATION, actual.getFilingPackage().getCourt().getLocation());
        Assertions.assertEquals(TestHelpers.PARTICIPATIONCLASS, actual.getFilingPackage().getCourt().getParticipatingClass());
        Assertions.assertEquals(TestHelpers.PROPERTYCLASS, actual.getFilingPackage().getCourt().getCourtClass());
        Assertions.assertEquals(TestHelpers.COURT_DESCRIPTION, actual.getFilingPackage().getCourt().getLocationDescription());
        Assertions.assertEquals(TestHelpers.CLASS_DESCRIPTION, actual.getFilingPackage().getCourt().getClassDescription());
        Assertions.assertEquals(TestHelpers.LEVEL_DESCRIPTION, actual.getFilingPackage().getCourt().getLevelDescription());
        Assertions.assertEquals(TestHelpers.TYPE, actual.getFilingPackage().getDocuments().get(0).getType());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getFilingPackage().getDocuments().get(0).getDescription());
        Assertions.assertEquals(true, actual.getFilingPackage().getDocuments().get(0).getIsAmendment());
        Assertions.assertEquals(true, actual.getFilingPackage().getDocuments().get(0).getIsSupremeCourtScheduling());
        Assertions.assertEquals(true, actual.isRushedSubmission());
    }

}
