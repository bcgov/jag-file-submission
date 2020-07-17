package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GenerateUrlPayload {

    private String DIVISION = null;
    private String FILENUMBER = null;
    private String LEVEL = null;
    private String LOCATION = null;
    private String PARTICIPATIONCLASS = null;
    private String PROPERTYCLASS = null;
    private String NAME = null;
    private String DESCRIPTION = null;
    private String TYPEFIRST = null;
    private String TYPESECOND = null;
    private String DISPLAYNAME = null;

    public String SUCCESS_URL = null;
    public String CANCEL_URL = null;
    public String ERROR_URL = null;

    private ClientApplication clientApplication;
    private FilingPackage filingPackage;
    private Navigation navigation;
    private GenerateUrlRequest generateUrlRequest;

    public String validGenerateUrlPayload() throws IOException {
        generateUrlRequest = new GenerateUrlRequest();

        TYPEFIRST = "string";
        DISPLAYNAME = "string";

        DIVISION = "string";
        FILENUMBER = "string";
        LEVEL = "string";
        LOCATION = "string";
        PARTICIPATIONCLASS ="string";
        PROPERTYCLASS = "string";
        NAME = "string";
        DESCRIPTION = "string";
        TYPESECOND = "DFCL";

        SUCCESS_URL = "http://success";
        CANCEL_URL = "http://cancel";
        ERROR_URL = "http://error";

        ObjectMapper objMap = new ObjectMapper();
        return objMap.writeValueAsString(generateUrlRequestPayload(clientApplication, filingPackage, navigation));
    }

    public GenerateUrlRequest generateUrlRequestPayload(ClientApplication clientApplication, FilingPackage filingPackage,Navigation navigation){
        generateUrlRequest = new GenerateUrlRequest();

        generateUrlRequest.setClientApplication(createClientApplication(DISPLAYNAME,TYPEFIRST));
        generateUrlRequest.setFilingPackage(createPackage(createCourt(), createDocumentList()));
        generateUrlRequest.setNavigation(createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL));

        return generateUrlRequest;
    }

    public ClientApplication createClientApplication(String displayName, String type) {
        clientApplication = new ClientApplication();

        clientApplication.setDisplayName(displayName);
        clientApplication.setType(type);

        return clientApplication;
    }

    public FilingPackage createPackage(Court court, List<DocumentProperties> documents) {
        filingPackage = new FilingPackage();

        filingPackage.setCourt(court);
        filingPackage.setDocuments(documents);

        return filingPackage;
    }

    public Navigation createNavigation(String success, String cancel, String error) {
        navigation = new Navigation();
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

    public Court createCourt() {
        Court court = new Court();

        court.setDivision(DIVISION);
        court.setFileNumber(FILENUMBER);
        court.setLevel(LEVEL);
        court.setLocation(LOCATION);
        court.setParticipatingClass(PARTICIPATIONCLASS);
        court.setPropertyClass(PROPERTYCLASS);

        return court;
    }

    public List<DocumentProperties> createDocumentList() {
        DocumentProperties documentProperties = new DocumentProperties();

        documentProperties.setName(NAME);
        documentProperties.setType(TYPESECOND);
        documentProperties.setDescription(DESCRIPTION);

        return Arrays.asList(documentProperties);
    }

    public Navigation createDefaultNavigation() {
        return createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }
}
