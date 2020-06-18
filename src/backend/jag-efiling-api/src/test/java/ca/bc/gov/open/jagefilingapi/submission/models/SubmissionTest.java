package ca.bc.gov.open.jagefilingapi.submission.models;

import ca.bc.gov.open.jagefilingapi.TestHelpers;
import ca.bc.gov.open.jagefilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jagefilingapi.api.model.EndpointAccess;
import ca.bc.gov.open.jagefilingapi.api.model.Navigation;
import ca.bc.gov.open.jagefilingapi.api.model.Redirect;
import ca.bc.gov.open.jagefilingapi.fee.models.Fee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Collections;
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

    @Test
    @DisplayName("CASE 1: testing constructor")
    public void testingConstructor() {
        Fee fee = new Fee(BigDecimal.TEN);

        Submission actual = new Submission(UUID.randomUUID(), TestHelpers.createDocumentProperties(HEADER, URL, SUBTYPE, TYPE), TestHelpers.createNavigation(CASE_1, CANCEL, ERROR), fee);

        Assertions.assertEquals(TYPE, actual.getDocumentProperties().getType());
        Assertions.assertEquals(SUBTYPE, actual.getDocumentProperties().getSubType());
        Assertions.assertEquals(URL, actual.getDocumentProperties().getSubmissionAccess().getUrl());
        Assertions.assertEquals(EndpointAccess.VerbEnum.POST, actual.getDocumentProperties().getSubmissionAccess().getVerb());
        Assertions.assertEquals(ERROR, actual.getNavigation().getError().getUrl());
        Assertions.assertEquals(CANCEL, actual.getNavigation().getCancel().getUrl());
        Assertions.assertEquals(CASE_1, actual.getNavigation().getSuccess().getUrl());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFee().getAmount());
    }

}
