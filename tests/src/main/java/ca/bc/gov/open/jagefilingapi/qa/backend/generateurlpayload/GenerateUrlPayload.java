package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GenerateUrlPayload {

    private static final String DIVISION = "division";
    private static final String FILENUMBER = "file number";
    private static final String LEVEL = "level";
    private static final String LOCATION = "location";
    private static final String PARTICIPATIONCLASS = "class";
    private static final String PROPERTYCLASS = "property class";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TYPEFIRST = "Client application type";
    private static final String TYPESECOND = "Document type";
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

        generateUrlRequest.setClientApplication(createClientApplication(DISPLAYNAME,TYPEFIRST));
       // generateUrlRequest.setFilingPackage(createPackage(createCourtDetails(), createDocumentsList()));
        generateUrlRequest.setNavigation(createNavigationDetails(SUCCESS_URL, CANCEL_URL, ERROR_URL));

        return generateUrlRequest;
    }

    public ClientApplication createClientApplication(String displayName, String type) {
        ClientApplication clientApplication = new ClientApplication();

        clientApplication.setDisplayName(displayName);
        clientApplication.setType(type);

        return clientApplication;
    }

    public FilingPackage createPackage(Court court, List<DocumentProperties> documents) {
        FilingPackage filingPackage = new FilingPackage();

        filingPackage.setCourt(court);
       // filingPackage.setDocuments(documents);

        return filingPackage;
    }

    public Navigation createNavigationDetails(String success, String cancel, String error) {
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

    public Court createCourtDetails() {
        Court court = new Court();

        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setPropertyClass(PROPERTYCLASS);

        return court;
    }

    public List<DocumentProperties> createDocumentsList() {
        DocumentProperties documentProperties = new DocumentProperties();

        documentProperties.setName(NAME);
        documentProperties.setType(TYPESECOND);
    //    documentProperties.setDescription(DESCRIPTION);

        return Arrays.asList(documentProperties);
    }

    public Navigation createDefaultNavigation() {
        return createNavigationDetails(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }
}
