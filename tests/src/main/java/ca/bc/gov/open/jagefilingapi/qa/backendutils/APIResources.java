package ca.bc.gov.open.jagefilingapi.qa.backendutils;

public enum APIResources {

    GENERATE_URL_API("/submission/"),
    INCORRECT_GENERATE_URL_API("/submissions/generateUrl"),
    SUBMISSION("/submission/"),
    DOCUMENT_SUBMISSION("/submission/documents"),
    EFILING_URL("http://localhost:3000/efiling"),
    CREATE_CSO_ACCOUNT_API("/csoAccount"),
    INCORRECT_CREATE_CSO_ACCOUNT_API("/csoAccounts");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
