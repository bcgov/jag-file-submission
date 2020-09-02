package ca.bc.gov.open.jag.efilingapi.submission.models;

import ca.bc.gov.open.jag.efilingcommons.model.Court;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilingPakageTest {


    public static final BigDecimal AGENCY_ID = BigDecimal.ONE;
    public static final String LOCATION = "location";
    public static final String LOCATION_DESCRIPTION = "locationDescription";
    public static final String LEVEL = "level";
    public static final String LEVEL_DESCRIPTION = "levelDescription";
    public static final String COURT_CLASS = "courtClass";
    public static final String CLASS_DESCRIPTION = "classDescription";
    public static final String DIVISION = "division";
    public static final String FILE_NUMBER = "fileNumber";
    public static final String PARTICIPATING_CLASS = "participatingClass";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String SUB_TYPE = "subType";
    public static final String DESCRIPTION = "description";
    public static final BigDecimal STATUTORY_FEE_AMOUNT = BigDecimal.TEN;
    public static final String MIME_TYPE = "mimeType";
    public static final BigDecimal PARTY_ID = BigDecimal.ONE;
    public static final String PARTY_TYPE_CD = "partyTypeCd";
    public static final String ROLE_TYPE_CD = "roleTypeCd";
    public static final String FIRST_NAME = "firstName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String LAST_NAME = "lastName";
    public static final String NAME_TYPE_CD = "nameTypeCd";
    public static final BigDecimal SUBMISSION_FEE_AMOUNT = BigDecimal.TEN;
    public static final String SERVER_FILE_NAME = "SERVER.txt";
    public static final String APP_TYPE = "appType";

    @Test
    public void testConstructor() {


        FilingPackage actual = new FilingPackage(
                SUBMISSION_FEE_AMOUNT,
                getCourt(),
                getDocuments(),
                getParties(),
                APP_TYPE,
                false);

        Assertions.assertEquals(STATUTORY_FEE_AMOUNT, actual.getSubmissionFeeAmount());
        Assertions.assertEquals(APP_TYPE, actual.getApplicationType());
        Assertions.assertFalse(actual.isRushedSubmission());
        assertCourt(actual);
        assertDocuments(actual);
        assertParties(actual);
    }

    private void assertParties(FilingPackage actual) {
        Assertions.assertEquals(PARTY_TYPE_CD, actual.getParties().get(0).getPartyTypeCd());
        Assertions.assertEquals(ROLE_TYPE_CD, actual.getParties().get(0).getRoleTypeCd());
        Assertions.assertEquals(FIRST_NAME, actual.getParties().get(0).getFirstName());
        Assertions.assertEquals(MIDDLE_NAME, actual.getParties().get(0).getMiddleName());
        Assertions.assertEquals(LAST_NAME, actual.getParties().get(0).getLastName());
        Assertions.assertEquals(NAME_TYPE_CD, actual.getParties().get(0).getNameTypeCd());
    }

    private void assertDocuments(FilingPackage actual) {
        Assertions.assertEquals(NAME, actual.getDocuments().get(0).getName());
        Assertions.assertEquals(TYPE, actual.getDocuments().get(0).getType());
        Assertions.assertEquals(SUB_TYPE, actual.getDocuments().get(0).getSubType());
        Assertions.assertEquals(true, actual.getDocuments().get(0).getIsAmendment());
        Assertions.assertEquals(false, actual.getDocuments().get(0).getIsSupremeCourtScheduling());
        Assertions.assertEquals("test", actual.getDocuments().get(0).getData());
        Assertions.assertEquals(DESCRIPTION, actual.getDocuments().get(0).getDescription());
        Assertions.assertEquals(STATUTORY_FEE_AMOUNT, actual.getDocuments().get(0).getStatutoryFeeAmount());
        Assertions.assertEquals(MIME_TYPE, actual.getDocuments().get(0).getMimeType());
    }

    private void assertCourt(FilingPackage actual) {
        Assertions.assertEquals(AGENCY_ID, actual.getCourt().getAgencyId());
        Assertions.assertEquals(LOCATION, actual.getCourt().getLocation());
        Assertions.assertEquals(LOCATION_DESCRIPTION, actual.getCourt().getLocationDescription());
        Assertions.assertEquals(LEVEL, actual.getCourt().getLevel());
        Assertions.assertEquals(LEVEL_DESCRIPTION, actual.getCourt().getLevelDescription());
        Assertions.assertEquals(COURT_CLASS, actual.getCourt().getCourtClass());
        Assertions.assertEquals(CLASS_DESCRIPTION, actual.getCourt().getClassDescription());
        Assertions.assertEquals(DIVISION, actual.getCourt().getDivision());
        Assertions.assertEquals(FILE_NUMBER, actual.getCourt().getFileNumber());
        Assertions.assertEquals(PARTICIPATING_CLASS, actual.getCourt().getParticipatingClass());
    }

    private List<Party> getParties() {
        List<Party> parties = new ArrayList<>();
        Party party = new Party(
                PARTY_TYPE_CD,
                ROLE_TYPE_CD,
                FIRST_NAME,
                MIDDLE_NAME,
                LAST_NAME,
                NAME_TYPE_CD);
        parties.add(party);
        return parties;
    }

    private List<Document> getDocuments() {
        List<Document> documents = new ArrayList<>();
        Document document = new Document(NAME,
                TYPE,
                SUB_TYPE,
                true,
                false,
                "test",
                DESCRIPTION,
                STATUTORY_FEE_AMOUNT,
                MIME_TYPE,
                SERVER_FILE_NAME);
        documents.add(document);
        return documents;
    }

    private Court getCourt() {
        return new Court(AGENCY_ID,
                    LOCATION,
                    LOCATION_DESCRIPTION,
                    LEVEL,
                    LEVEL_DESCRIPTION,
                    COURT_CLASS,
                    CLASS_DESCRIPTION,
                    DIVISION,
                    FILE_NUMBER,
                    PARTICIPATING_CLASS);
    }
}
