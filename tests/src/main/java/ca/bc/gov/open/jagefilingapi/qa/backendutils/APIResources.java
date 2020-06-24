package ca.bc.gov.open.jagefilingapi.qa.backendutils;

public enum APIResources {

    GENERATEURLAPI("/submission/generateUrl"),
    USERDETAIL("/submission/"),
    EFILINGURL("http://localhost:3000/efiling?");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
