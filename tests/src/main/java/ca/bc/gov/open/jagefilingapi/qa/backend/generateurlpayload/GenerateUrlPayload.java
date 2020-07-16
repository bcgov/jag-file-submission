package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

import ca.bc.gov.open.jag.efilingapi.qa.api.model.*;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private String TYPE = null;
    private String DISPLAYNAME = null;

    private String SUCCESS_URL = null;
    private String CANCEL_URL = null;
    private String ERROR_URL = null;

    private ModelPackage modelPackage;
    private Navigation navigation;
    private ClientApplication clientApplication;
    private GenerateUrlRequest generateUrlRequest;

    public String validGenerateUrlPayload() throws IOException {
        generateUrlRequest = new GenerateUrlRequest();

        DIVISION = "string";
        FILENUMBER = "string";
        LEVEL = "string";
        LOCATION = "string";
        PARTICIPATIONCLASS ="string";
        PROPERTYCLASS = "string";
        NAME = "string";
        DESCRIPTION = "string";
        TYPE = "string";
        DISPLAYNAME = "string";

        SUCCESS_URL = "http://success";
        CANCEL_URL = "http://cancel";
        ERROR_URL = "http://error";

        ObjectMapper objMap = new ObjectMapper();
        System.out.println(generateUrlRequestPayload(clientApplication, modelPackage, navigation).toString());
        return objMap.writeValueAsString(generateUrlRequestPayload(clientApplication, modelPackage, navigation));
    }

    public String invalidGenerateUrlPayload() throws JsonProcessingException {
        generateUrlRequest = new GenerateUrlRequest();

        DIVISION = "tst";
        FILENUMBER = "3535";
        LEVEL = "time";
        LOCATION = "test";
        PARTICIPATIONCLASS = "get";
        PROPERTYCLASS = "pst";
        NAME = "some";
        DESCRIPTION = "mopre";
        TYPE = "tet";
        DISPLAYNAME = "dp";

        SUCCESS_URL = "http://success";
        CANCEL_URL = "http://cancel";
        ERROR_URL = "http://error";

        ObjectMapper objMap = new ObjectMapper();
        System.out.println(generateUrlRequestPayload(clientApplication, modelPackage, navigation).toString());
        return objMap.writeValueAsString(generateUrlRequestPayload(clientApplication, modelPackage, navigation));
    }

    public GenerateUrlRequest generateUrlRequestPayload(ClientApplication clientApplication, ModelPackage modelPackage,Navigation navigation){
        generateUrlRequest = new GenerateUrlRequest();
        generateUrlRequest.setClientApplication(createClientApplication(DISPLAYNAME,TYPE));
        generateUrlRequest.setPackage(createPackage(createCourt(), createDocumentList()));
        generateUrlRequest.setNavigation(createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL));

        return generateUrlRequest;
    }

    public ModelPackage createPackage(Court court, List<DocumentProperties> documents) {
        modelPackage = new ModelPackage();

        modelPackage.setCourt(court);
        modelPackage.setDocuments(documents);

        return modelPackage;
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

    public ClientApplication createClientApplication(String displayName, String type) {
        clientApplication = new ClientApplication();

        clientApplication.setDisplayName(displayName);
        clientApplication.setType(type);

        return clientApplication;
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
        documentProperties.setType(TYPE);
        documentProperties.setDescription(DESCRIPTION);

        return Arrays.asList(documentProperties);
    }

    public Navigation createDefaultNavigation() {
        return createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }
}
