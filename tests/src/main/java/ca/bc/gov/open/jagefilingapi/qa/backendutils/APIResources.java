package ca.bc.gov.open.jagefilingapi.qa.backendutils;

public enum APIResources {

    GENERATEURLAPI("/submission/generateUrl"),
    GETCONFIGURATION("/submission/getConfiguration"),
    EFILINGURL("https://httpbin.org");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}


