package ca.bc.gov.open.jag.efilingapi;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.submission.models.SubmissionConstants;
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

    public static InitialPackage createInitalPackage(Court court, List<DocumentProperties> documentProperties) {
        InitialPackage initialPackage = new InitialPackage();
        initialPackage.setCourt(court);
        initialPackage.setDocuments(documentProperties);
        return initialPackage;
    }

    public static FilingPackage createApiPackage(Court court, List<Document> documents, List<Party> parties) {
        FilingPackage modelPackage = new FilingPackage();
        modelPackage.setCourt(court);
        modelPackage.setDocuments(documents);
        modelPackage.setParties(parties);
        return modelPackage;
    }


    public static Navigation createNavigation(String success, String cancel, String error) {
        Navigation navigation = new Navigation();
        Redirect successRedirect = new Redirect();
        successRedirect.setUrl(success);
        navigation.setSuccess(successRedirect);
        Redirect cancelRedirect = new Redirect();
        cancelRedirect.setUrl(cancel);
        navigation.setCancel(cancelRedirect);
        Redirect errorRedirect = new Redirect();
        errorRedirect.setUrl(error);
        navigation.setError(errorRedirect);
        return navigation;
    }

    public static ClientApplication createClientApplication(String displayName, String type) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setDisplayName(displayName);
        clientApplication.setType(type);

        return clientApplication;
    }

    public static Court createApiCourt() {
        Court court = new Court();
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

    public static List<DocumentProperties> createApiDocumentPropertiesList() {
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setName("random.txt");
        documentProperties.setType(TYPE);
        return Arrays.asList(documentProperties);
    }

    public static List<Document> createApiDocumentList() {
        Document documentProperties = new Document();
        documentProperties.setDescription(DESCRIPTION);
        documentProperties.setStatutoryFeeAmount(BigDecimal.TEN);
        documentProperties.setName("random.txt");
        documentProperties.setType(TYPE);
        documentProperties.setSubType(SubmissionConstants.SUBMISSION_ORDR_DOCUMENT_SUB_TYPE_CD);
        documentProperties.setMimeType("application/txt");
        documentProperties.setIsSupremeCourtScheduling(true);
        documentProperties.setIsAmendment(true);
        documentProperties.setData(new JsonObject());
        return Arrays.asList(documentProperties);
    }

    public static List<Party> createApiPartyList() {

        Party partyOne = new Party();
        partyOne.setFirstName(FIRST_NAME);
        partyOne.setMiddleName(MIDDLE_NAME);
        partyOne.setLastName(LAST_NAME);
        partyOne.setNameTypeCd(NAME_TYPE_CD);
        partyOne.setPartyTypeCd(PARTY_TYPE_CD);
        partyOne.setRoleTypeCd(ROLE_TYPE_CD);
        partyOne.setPartyId(BigDecimal.ONE);

        Party partyTwo = new Party();
        partyTwo.setFirstName(FIRST_NAME);
        partyTwo.setMiddleName(MIDDLE_NAME);
        partyTwo.setLastName(LAST_NAME);
        partyTwo.setNameTypeCd(NAME_TYPE_CD);
        partyTwo.setPartyTypeCd(PARTY_TYPE_CD);
        partyTwo.setRoleTypeCd(ROLE_TYPE_CD);
        partyTwo.setPartyId(BigDecimal.TEN);

        return Arrays.asList(partyOne, partyTwo);
    }

    public static ca.bc.gov.open.jag.efilingapi.submission.models.Court createCourt() {
        return ca.bc.gov.open.jag.efilingapi.submission.models.Court.builder()
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

    public static ca.bc.gov.open.jag.efilingapi.submission.models.FilingPackage createPackage(
            ca.bc.gov.open.jag.efilingapi.submission.models.Court court,
            List<ca.bc.gov.open.jag.efilingapi.submission.models.Document> documents,
            List<ca.bc.gov.open.jag.efilingapi.submission.models.Party> parties) {
        return ca.bc.gov.open.jag.efilingapi.submission.models.FilingPackage.builder()
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

    public static List<ca.bc.gov.open.jag.efilingapi.submission.models.Document> createDocumentList() {
        return Arrays.asList(ca.bc.gov.open.jag.efilingapi.submission.models.Document.builder()
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

    public static List<ca.bc.gov.open.jag.efilingapi.submission.models.Party> createPartyList() {

        ca.bc.gov.open.jag.efilingapi.submission.models.Party partyOne = ca.bc.gov.open.jag.efilingapi.submission.models.Party.builder()
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME)
                .lastName(LAST_NAME)
                .nameTypeCd(NAME_TYPE_CD)
                .partyTypeCd(PARTY_TYPE_CD)
                .roleTypeCd(ROLE_TYPE_CD)
                .partyId(BigDecimal.ONE)
                .create();

        ca.bc.gov.open.jag.efilingapi.submission.models.Party partyTwo = ca.bc.gov.open.jag.efilingapi.submission.models.Party.builder()
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME)
                .lastName(LAST_NAME)
                .nameTypeCd(NAME_TYPE_CD)
                .partyTypeCd(PARTY_TYPE_CD)
                .roleTypeCd(ROLE_TYPE_CD)
                .partyId(BigDecimal.TEN)
                .create();
        return Arrays.asList(partyOne, partyTwo);
    }

    public static Navigation createDefaultNavigation() {
        return createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }


}
