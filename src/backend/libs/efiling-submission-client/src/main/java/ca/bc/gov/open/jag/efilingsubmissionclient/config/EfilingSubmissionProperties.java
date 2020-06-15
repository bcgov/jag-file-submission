package ca.bc.gov.open.jag.efilingsubmissionclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jag.efiling.submission.client")
public class EfilingSubmissionProperties {

    private String filingSubmissionSoapUri;

    public String getFilingSubmissionSoapUri() {
        return filingSubmissionSoapUri;
    }

    public void setFilingSubmissionSoapUri(String filingSubmissionSoapUri) {
        this.filingSubmissionSoapUri = filingSubmissionSoapUri;
    }
}
