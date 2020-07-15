package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class Payload {

    Documents documents;
    Navigation navigation;

    public Payload(Documents documents, Navigation navigation) {
        this.documents = documents;
        this.navigation = navigation;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }
}
