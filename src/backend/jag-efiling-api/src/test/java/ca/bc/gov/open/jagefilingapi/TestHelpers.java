package ca.bc.gov.open.jagefilingapi;

import ca.bc.gov.open.jagefilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jagefilingapi.api.model.EndpointAccess;
import ca.bc.gov.open.jagefilingapi.api.model.Navigation;
import ca.bc.gov.open.jagefilingapi.api.model.Redirect;

import java.util.Collections;

public class TestHelpers {
    public static DocumentProperties createDocumentProperties(String HEADER, String URL, String SUBTYPE, String TYPE) {
        DocumentProperties documentProperties = new DocumentProperties();
        EndpointAccess documentAccess = new EndpointAccess();
        documentAccess.setHeaders(Collections.singletonMap(HEADER, HEADER));
        documentAccess.setUrl(URL);
        documentAccess.setVerb(EndpointAccess.VerbEnum.POST);
        documentProperties.setSubmissionAccess(documentAccess);
        documentProperties.setSubType(SUBTYPE);
        documentProperties.setType(TYPE);
        return documentProperties;
    }

    public static Navigation createNavigation(String CASE_1, String CANCEL, String ERROR) {
        Navigation navigation = new Navigation();
        Redirect successRedirect = new Redirect();
        successRedirect.setUrl(CASE_1);
        navigation.setSuccess(successRedirect);
        Redirect cancelRedirect = new Redirect();
        cancelRedirect.setUrl(CANCEL);
        navigation.setCancel(cancelRedirect);
        Redirect errorRedirect = new Redirect();
        errorRedirect.setUrl(ERROR);
        navigation.setError(errorRedirect);
        return navigation;
    }
}
