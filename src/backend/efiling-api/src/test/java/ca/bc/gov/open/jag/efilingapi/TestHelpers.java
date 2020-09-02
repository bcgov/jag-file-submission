package ca.bc.gov.open.jag.efilingapi;


import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingcommons.model.Court;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.model.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingapi.submission.models.*;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TestHelpers {

    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    public static final UUID CASE_2 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID CASE_3 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    public static final UUID CASE_4 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fc");
    public static final UUID CASE_5 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fd");

    public static final String DIVISION = "DIVISION";
    public static final String FILENUMBER = "FILENUMBER";
    public static final String LEVEL = "LEVEL";
    public static final String LOCATION = "1211";
    public static final String PARTICIPATIONCLASS = "PARTICIPATIONCLASS";
    public static final String PROPERTYCLASS = "PROPERTYCLASS";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TYPE = "TYPE";
    public static final String COURT_DESCRIPTION = "TESTCOURTDESC";
    public static final String LEVEL_DESCRIPTION = "TESTLEVELDESC";
    public static final String CLASS_DESCRIPTION = "TESTCLASSDESC";
    public static final String DISPLAY_NAME = "DISPLAYNAME";

    public static final String SUCCESS_URL = "http://success";
    public static final String CANCEL_URL = "http://cancel";
    public static final String ERROR_URL = "http://error";

    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String MIDDLE_NAME = "MIDDLENAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String NAME_TYPE_CD = "NAMECD";
    public static final String PARTY_TYPE_CD = "PARTYCD";
    public static final String ROLE_TYPE_CD = "ROLECD";

    public static InitialPackage createInitalPackage(ca.bc.gov.open.jag.efilingapi.api.model.Court court, List<DocumentProperties> documentProperties) {
        InitialPackage initialPackage = new InitialPackage();
        initialPackage.setCourt(court);
        initialPackage.setDocuments(documentProperties);
        return initialPackage;
    }

    public static Navigation createNavigation(String success, String cancel, String error) {
        Navigation navigation = new Navigation();
        navigation.setSuccess(success);
        navigation.setCancel(cancel);
        navigation.setError(error);
        return navigation;
    }

    public static ClientApplication createClientApplication(String displayName, String type) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setDisplayName(displayName);
        clientApplication.setType(type);

        return clientApplication;
    }

    public static ca.bc.gov.open.jag.efilingapi.api.model.Court createApiCourt() {
        ca.bc.gov.open.jag.efilingapi.api.model.Court court = new ca.bc.gov.open.jag.efilingapi.api.model.Court();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setCourtClass(PROPERTYCLASS);
        court.setAgencyId(BigDecimal.TEN);
        court.setLevelDescription(LEVEL_DESCRIPTION);
        court.setClassDescription(CLASS_DESCRIPTION);
        court.setLocationDescription(COURT_DESCRIPTION);
        return court;
    }

    public static Court createCourt() {
        return Court.builder()
                .division(DIVISION)
                .fileNumber(FILENUMBER)
                .level(LEVEL)
                .location(LOCATION)
                .participatingClass(PARTICIPATIONCLASS)
                .courtClass(PROPERTYCLASS)
                .agencyId(BigDecimal.TEN)
                .levelDescription(LEVEL_DESCRIPTION)
                .classDescription(CLASS_DESCRIPTION)
                .locationDescription(COURT_DESCRIPTION).create();
    }

    public static Submission buildSubmission() {
        return Submission
                .builder()
                .filingPackage(createPackage(createCourt(), createDocumentList(), createPartyList()))
                .navigation(createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL))
                .create();
    }

    public static FilingPackage createPackage(
            Court court,
            List<Document> documents,
            List<Party> parties) {
        return FilingPackage.builder()
                .court(court)
                .documents(documents)
                .parties(parties)
                .create();
    }

    public static List<DocumentProperties> createDocumentPropertiesList() {
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setName("random.txt");
        documentProperties.setType(TYPE);
        return Arrays.asList(documentProperties);
    }

    public static List<Document> createDocumentList() {
        return Arrays.asList(Document.builder()
                .description(DESCRIPTION)
                .statutoryFeeAmount(BigDecimal.TEN)
                .name("random.txt")
                .type(TYPE)
                .subType(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD)
                .mimeType("application/txt")
                .isSupremeCourtScheduling(true)
                .isAmendment(true)
                .data(new JsonObject()).create());
    }

    public static List<Party> createPartyList() {

        Party partyOne = buildParty(BigDecimal.ONE);

        Party partyTwo = buildParty(BigDecimal.TEN);
        return Arrays.asList(partyOne, partyTwo);
    }

    private static Party buildParty(BigDecimal partyId) {
        return Party.builder()
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME)
                .lastName(LAST_NAME)
                .nameTypeCd(NAME_TYPE_CD)
                .partyTypeCd(PARTY_TYPE_CD)
                .roleTypeCd(ROLE_TYPE_CD)
                .create();
    }

    public static Navigation createDefaultNavigation() {
        return createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }


}
