package ca.bc.gov.open.jagefilingapi.qa.backendutils;

public enum APIResources {

    generateUrlAPI("/submission/generateUrl"),
    getConfiguration("/submission/getConfiguration"),
    eFilingUrl("https://httpbin.org");

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}

