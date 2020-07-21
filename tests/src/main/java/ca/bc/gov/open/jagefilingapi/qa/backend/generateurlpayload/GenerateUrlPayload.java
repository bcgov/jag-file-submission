package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GenerateUrlPayload {

    private static final String DIVISION = "division";
    private static final String FILENUMBER = "file number";
    private static final String LEVEL = "level";
    private static final String LOCATION = "location";
    private static final String PARTICIPATIONCLASS = "class";
    private static final String PROPERTYCLASS = "property class";
    private static final String DESCRIPTION = "description";
    private static final String APPTYPE = "Client application type";
    private static final String DOCTYPE = "Document type";
    private static final String DISPLAYNAME = "Display name";

    public static final String SUCCESS_URL = "http://success.com";
    public static final String CANCEL_URL = "http://cancel.com";
    public static final String ERROR_URL = "http://error.com";

    private GenerateUrlRequest generateUrlRequest;

    public String validGenerateUrlPayload() throws IOException {
        generateUrlRequest = new GenerateUrlRequest();

        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(generateUrlRequestPayload());
    }

    public GenerateUrlRequest generateUrlRequestPayload(){
        generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(generateClientApplication(DISPLAYNAME, APPTYPE));
        generateUrlRequest.setFilingPackage(generateInitialPackage(generateCourt(), generateDocumentPropertiesList()));
        generateUrlRequest.setNavigation(generateNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL));

        return generateUrlRequest;
    }

    public static InitialPackage generateInitialPackage(Court court, List<DocumentProperties> documentProperties) {
        InitialPackage initialPackage = new InitialPackage();
        initialPackage.setCourt(court);
        initialPackage.setDocuments(documentProperties);
        return initialPackage;
    }

    public static FilingPackage generatePackage(Court court, List<Document> documents) {
        FilingPackage modelPackage = new FilingPackage();
        modelPackage.setCourt(court);
        modelPackage.setDocuments(documents);
        return modelPackage;
    }

    public static Navigation generateNavigation(String success, String cancel, String error) {
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

    public static ClientApplication generateClientApplication(String displayName, String type) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setDisplayName(displayName);
        clientApplication.setType(type);

        return clientApplication;
    }

    public static Court generateCourt() {
        Court court = new Court();
        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setPropertyClass(PROPERTYCLASS);

        return court;
    }

    public static List<DocumentProperties> generateDocumentPropertiesList() {
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setName("random.txt");
        documentProperties.setType(DOCTYPE);

        return Arrays.asList(documentProperties);
    }

    public static List<Document> generateDocumentList() {
        Document documentProperties = new Document();
        documentProperties.setDescription(DESCRIPTION);
        documentProperties.setStatutoryFeeAmount(BigDecimal.TEN);
        documentProperties.setName("random.txt");
        documentProperties.setType(DOCTYPE);

        return Arrays.asList(documentProperties);
    }

    public static Navigation generateDefaultNavigation() {
        return generateNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }
}
