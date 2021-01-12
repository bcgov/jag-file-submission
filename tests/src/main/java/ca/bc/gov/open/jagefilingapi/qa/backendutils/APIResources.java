package ca.bc.gov.open.jagefilingapi.qa.backendutils;

public enum APIResources {

    GENERATE_URL_API("/submission/"),
    SUBMISSION("/submission/"),
    DOCUMENT_SUBMISSION("/submission/documents"),
    CSO_ACCOUNT_API("/csoAccount"),
    BCEID_ACCOUNT_API("/bceidAccount"),
    LOOK_UP_API("/lookup/documentTypes"),
    PAYMENT_API("/payment/generate-update-card"),
    INCORRECT_GENERATE_URL_API("/submissions/generateUrl"),
    INCORRECT_CREATE_CSO_ACCOUNT_API("/csoAccounts"),
    COURTS_API("/courts");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }
    public String getResource() {
        return resource;
    }
}
