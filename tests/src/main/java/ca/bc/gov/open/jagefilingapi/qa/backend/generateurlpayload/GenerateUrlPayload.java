package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GenerateUrlPayload {

    private static final String LEVEL = "P";
    private static final String LOCATION = "1211";
    private static final String COURT_CLASS = "F";
    private static final String DESCRIPTION = "Without Notice Application Checklist";
    private static final String APPTYPE = "DCFL";
    private static final String DOCTYPE = "WNC";
    private static final String DISPLAYNAME = "my app";
    private static final String SUB_TYPE = "sub";
    private static final boolean IS_AMENDMENT = true;
    private static final boolean IS_SUPREME_COURT_SCHEDULING = true;
    private static final String DATA = "data";

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

        generateUrlRequest.setNavigation(generateNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL));
        generateUrlRequest.setClientApplication(generateClientApplication(DISPLAYNAME, APPTYPE));
        generateUrlRequest.setFilingPackage(generateInitialPackage(generateCourt(), generateDocumentPropertiesList()));

        return generateUrlRequest;
    }

    public GenerateUrlRequest generateUpdateDocumentsPayload() {
        generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setFilingPackage(generateInitialPackage(generateDocumentPropertiesList()));
        return generateUrlRequest;
    }

    public static InitialPackage generateInitialPackage( List<DocumentProperties> documentProperties) {
        InitialPackage initialPackage = new InitialPackage();
        initialPackage.setDocuments(documentProperties);
        return initialPackage;
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
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setCourtClass(COURT_CLASS);
        return court;
    }

    public static List<DocumentProperties> generateDocumentPropertiesList() {
        DocumentProperties documentProperties = new DocumentProperties();
        documentProperties.setName("time.ps1");
        documentProperties.setType(DOCTYPE);
        documentProperties.setSubType(SUB_TYPE);
        documentProperties.setIsAmendment(IS_AMENDMENT);
        documentProperties.setIsSupremeCourtScheduling(IS_SUPREME_COURT_SCHEDULING);
        documentProperties.setData(DATA);

        return Arrays.asList(documentProperties);
    }

    public static List<Document> generateDocumentList() {
        Document documentProperties = new Document();
        documentProperties.setDescription(DESCRIPTION);
        documentProperties.setStatutoryFeeAmount(BigDecimal.TEN);
        documentProperties.setName("time.ps1");
        documentProperties.setType(DOCTYPE);

        return Arrays.asList(documentProperties);
    }

    public static Navigation generateDefaultNavigation() {
        return generateNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }
}
