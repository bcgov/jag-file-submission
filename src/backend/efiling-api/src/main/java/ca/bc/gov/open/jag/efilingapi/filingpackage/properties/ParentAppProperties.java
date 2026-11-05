package ca.bc.gov.open.jag.efilingapi.filingpackage.properties;

public class ParentAppProperties {

    private String application;
    private String returnUrl;
    private Boolean rejectedDocuments;

    public String getApplication() { return application; }

    public void setApplication(String application) { this.application = application; }

    public String getReturnUrl() { return returnUrl; }

    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }

    public Boolean getRejectedDocuments() { return rejectedDocuments;  }

    public void setRejectedDocuments(Boolean rejectedDocuments) { this.rejectedDocuments = rejectedDocuments; }

}
