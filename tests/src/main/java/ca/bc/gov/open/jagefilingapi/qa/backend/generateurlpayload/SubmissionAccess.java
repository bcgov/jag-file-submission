package ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload;

public class SubmissionAccess {

    private String url;
    private String verb;
    Headers headers;

    public SubmissionAccess(String url, String verb, Headers headers) {
        this.url = url;
        this.verb = verb;
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
