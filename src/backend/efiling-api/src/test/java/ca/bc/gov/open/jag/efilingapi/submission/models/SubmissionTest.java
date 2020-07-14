package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.EndpointAccess;
import ca.bc.gov.open.jag.efilingapi.fee.models.Fee;
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
    private static final String SUBTYPE = "SUBTYPE";
    private static final String URL = "http://doc.com";
    private static final String HEADER = "HEADER";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String EMAIL = "email";
    private static final String APPLICATION_TYPE = "ApplicationType";
    private static final String COURT_LOCATION = "CourtLocation";
    private static final String COURT_LEVEL = "CourtLevel";
    private static final String COURT_DIVISION = "CourtDivision";
    private static final String COURT_CLASS = "CourtClass";
    private static final String PARTICIPATION_CLASS = "ParticipationClass";
    private static final String INDIGENOUS_STATUS = "IndigenousStatus";
    private static final String DOCUMENT_TYPE = "DocumentType";
    private static final String COURT_FILE_NUMBER = "CourtFileNumber";

    @Test
    @DisplayName("CASE 1: testing constructor")
    public void testingConstructor() {
        Fee fee = new Fee(BigDecimal.TEN);
        AccountDetails accountDetails = new AccountDetails(UUID.randomUUID(), BigDecimal.TEN, BigDecimal.ONE, true, FIRST_NAME, LAST_NAME, MIDDLE_NAME, EMAIL);

        Submission actual = new Submission(
                UUID.randomUUID(),
                TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE),
                TestHelpers.createNavigation(CASE_1, CANCEL, ERROR),
                TestHelpers.createClientApplication(APPLICATION_TYPE, COURT_LOCATION,
                                                COURT_LEVEL, COURT_DIVISION, COURT_CLASS,
                                                PARTICIPATION_CLASS, INDIGENOUS_STATUS,
                                                DOCUMENT_TYPE, COURT_FILE_NUMBER),
                fee,
                accountDetails,
                1);


        Assertions.assertEquals(TYPE, actual.getDocumentProperties().getType());
        Assertions.assertEquals(SUBTYPE, actual.getDocumentProperties().getSubType());
        Assertions.assertEquals(URL, actual.getDocumentProperties().getSubmissionAccess().getUrl());
        Assertions.assertEquals(EndpointAccess.VerbEnum.POST, actual.getDocumentProperties().getSubmissionAccess().getVerb());
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
        Assertions.assertEquals(APPLICATION_TYPE, actual.getClientApplication().getApplicationType());
        Assertions.assertEquals(COURT_LOCATION, actual.getClientApplication().getCourtLocation());
        Assertions.assertEquals(COURT_LEVEL, actual.getClientApplication().getCourtLevel());
        Assertions.assertEquals(COURT_DIVISION, actual.getClientApplication().getCourtDivision());
        Assertions.assertEquals(COURT_CLASS, actual.getClientApplication().getCourtClass());
        Assertions.assertEquals(PARTICIPATION_CLASS, actual.getClientApplication().getParticipationClass());
        Assertions.assertEquals(INDIGENOUS_STATUS, actual.getClientApplication().getIndigenousStatus());
        Assertions.assertEquals(DOCUMENT_TYPE, actual.getClientApplication().getDocumentType());
        Assertions.assertEquals(COURT_FILE_NUMBER, actual.getClientApplication().getCourtFileNumber());
    }

}
