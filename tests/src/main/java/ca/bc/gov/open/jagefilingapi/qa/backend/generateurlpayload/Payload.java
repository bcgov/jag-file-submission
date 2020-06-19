package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class Payload {

    DocumentProperties documentProperties;
    Navigation navigation;

    public Payload(DocumentProperties documentProperties, Navigation navigation) {
        this.documentProperties = documentProperties;
        this.navigation = navigation;
    }

    public DocumentProperties getDocumentProperties() {
        return documentProperties;
    }

    public void setDocumentProperties(DocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }
}
