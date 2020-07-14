package ca.bc.gov.open.jag.efilingapi;

import ca.bc.gov.open.jag.efilingapi.api.model.*;

import java.util.Collections;
import java.util.UUID;

public class TestHelpers {

    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    public static final UUID CASE_2 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID CASE_3 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");
    public static final UUID CASE_4 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fc");
    public static final UUID CASE_5 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fd");

    public static final String SUCCESS_URL = "http://success";
    public static final String CANCEL_URL = "http://cancel";
    public static final String ERROR_URL = "http://error";

    public static DocumentProperties createDocumentProperties(String header, String url, String subtype, String type) {
        DocumentProperties documentProperties = new DocumentProperties();
        EndpointAccess documentAccess = new EndpointAccess();
        documentAccess.setHeaders(Collections.singletonMap(header, header));
        documentAccess.setUrl(url);
        documentAccess.setVerb(EndpointAccess.VerbEnum.POST);
        documentProperties.setSubmissionAccess(documentAccess);
        documentProperties.setSubType(subtype);
        documentProperties.setType(type);
        return documentProperties;
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

    public static ClientApplication createClientApplication(String applicationType, String courtLocation, String courtLevel,
                                                            String courtDivision, String courtClass, String participationClass,
                                                            String indigenousStatus, String documentType, String courtFileNumber) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setApplicationType(applicationType);
        clientApplication.setCourtLocation(courtLocation);
        clientApplication.setCourtLevel(courtLevel);
        clientApplication.setCourtDivision(courtDivision);
        clientApplication.setCourtClass(courtClass);
        clientApplication.setParticipationClass(participationClass);
        clientApplication.setIndigenousStatus(indigenousStatus);
        clientApplication.setDocumentType(documentType);
        clientApplication.setCourtFileNumber(courtFileNumber);
        return clientApplication;
    }

    public static Navigation createDefaultNavigation() {
        return createNavigation(SUCCESS_URL, CANCEL_URL, ERROR_URL);
    }


}
