package ca.bc.gov.open.jag.efilingreviewerapi.error;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "errorEmail")
public class ErrorEmailProperties {

    private String toEmail;
    private String fromEmail;

    public String getToEmail() { return toEmail; }

    public void setToEmail(String toEmail) { this.toEmail = toEmail; }

    public String getFromEmail() {  return fromEmail;  }

    public void setFromEmail(String fromEmail) { this.fromEmail = fromEmail; }

}
