package ca.bc.gov.open.jagefilingapi.qa.backendutils;

public enum APIResources {

    GENERATE_URL_API("/submission/generateUrl"),
    INCORRECT_GENERATE_URL_API("/submissions/generateUrl"),
    SUBMISSION("/submission/"),
    EFILING_URL("http://localhost:3000/efiling"),
    CREATE_CSO_ACCOUNT_API("/csoAccount");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
