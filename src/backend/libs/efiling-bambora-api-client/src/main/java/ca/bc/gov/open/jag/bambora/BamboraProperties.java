package ca.bc.gov.open.jag.bambora;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.text.MessageFormat;
import java.util.Base64;

@ConfigurationProperties(prefix = "bambora")
public class BamboraProperties {
    private String apiBasePath;
    private String apiPasscode;
    private String merchantId;

    public String getApiBasePath() { return apiBasePath; }

    public void setApiBasePath(String apiBasePath) { this.apiBasePath = apiBasePath; }

    public String getApiPasscode() {
        return apiPasscode;
    }

    public void setApiPasscode(String apiPasscode) {
        this.apiPasscode = apiPasscode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getEncodedKey() {
       String apiKey = MessageFormat.format("{0}:{1}", this.merchantId,this.apiPasscode);
       return Base64.getEncoder().encodeToString(apiKey.getBytes());
    }
}
