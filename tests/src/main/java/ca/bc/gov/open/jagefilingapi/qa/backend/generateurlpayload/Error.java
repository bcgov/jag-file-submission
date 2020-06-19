package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class Error {

    private String url;

    public Error(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
