package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GenerateUrlPayload {

    public static final String DIVISION = "DIVISION";
    public static final String LEVEL = "P";
    public static final String LOCATION = "4801";
    private static final String COURT_CLASS = "F";
    public static final String PARTICIPATION_CLASS = "PARTICIPATIONCLASS";
    public static final String COURT_DESCRIPTION = "TESTCOURTDESC";
    public static final String LEVEL_DESCRIPTION = "TESTLEVELDESC";
    public static final String CLASS_DESCRIPTION = "TESTCLASSDESC";

    public static final String CLIENT_APP_NAME = "Efiling demo";

    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String MIDDLE_NAME = "MIDDLENAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String MD5 = "Md 5";
    public static final DocumentProperties.TypeEnum TYPE = DocumentProperties.TypeEnum.AAB;

    public static final String SUCCESS_URL = "http://success.com";
    public static final String CANCEL_URL = "http://cancel.com";
    public static final String ERROR_URL = "http://error.com";

    private GenerateUrlRequest generateUrlRequest;

    public String validGenerateUrlPayload() throws IOException {
        generateUrlRequest = new GenerateUrlRequest();
        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(generateUrlRequestPayload());
    }

    public String updatePropertiesPayload() throws IOException {
        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(updatePropertiesPackage());
    }

    public GenerateUrlRequest generateUrlRequestPayload(){
        generateUrlRequest = new GenerateUrlRequest();
        generateUrlRequest.setClientAppName(CLIENT_APP_NAME);
        generateUrlRequest.setFilingPackage(generateInitialPackage(generateCourt(), generateDocumentPropertiesList(), generatePartyList()));
        generateUrlRequest.setNavigationUrls(generateNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL));
        return generateUrlRequest;
    }

    public static UpdateDocumentRequest updatePropertiesPackage() {
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();
        updateDocumentRequest.setDocuments(generateDocumentPropertiesList());
        return updateDocumentRequest;
    }

    public static InitialPackage generateInitialPackage(Court court, List<DocumentProperties> documentProperties, List<Party> parties) {
        InitialPackage initialPackage = new InitialPackage();

        initialPackage.setCourt(court);
        initialPackage.setDocuments(documentProperties);
        initialPackage.setParties(parties);
        return initialPackage;
    }

    public static NavigationUrls generateNavigation(String success, String cancel, String error) {
        NavigationUrls navigation = new NavigationUrls();

        navigation.setSuccess(success);
        navigation.setCancel(cancel);
        navigation.setError(error);
        return navigation;
    }

    public static Court generateCourt() {
        Court court = new Court();

        court.setDivision(DIVISION);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATION_CLASS);
        court.setCourtClass(COURT_CLASS);
        court.setAgencyId(BigDecimal.TEN);
        court.setLevelDescription(LEVEL_DESCRIPTION);
        court.setClassDescription(CLASS_DESCRIPTION);
        court.setLocationDescription(COURT_DESCRIPTION);
        return court;
    }

    public static List<DocumentProperties> generateDocumentPropertiesList() {
        DocumentProperties documentProperties = new DocumentProperties();

        documentProperties.setName("test-document.pdf");
        documentProperties.setType(DocumentProperties.TypeEnum.AFF);
        documentProperties.setIsAmendment(true);
        documentProperties.setIsSupremeCourtScheduling(true);
        documentProperties.setData(new ObjectMapper().createObjectNode());
        documentProperties.setMd5(MD5);
        return Arrays.asList(documentProperties);
    }

    public static List<Party> generatePartyList() {
        Party parties = new Party();

        parties.setFirstName(FIRST_NAME);
        parties.setMiddleName(MIDDLE_NAME);
        parties.setLastName(LAST_NAME);
        parties.setRoleType(Party.RoleTypeEnum.APP);
        parties.setPartyType(Party.PartyTypeEnum.IND);

        return Arrays.asList(parties);
    }

    public static NavigationUrls createDefaultNavigation() {
        return generateNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }
}
