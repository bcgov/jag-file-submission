package ca.bc.gov.open.jag.bambora;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.ProfilesApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiClient;
import ca.bc.gov.open.jag.efilingcommons.payment.PaymentAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

@Configuration
@EnableConfigurationProperties(BamboraProperties.class)
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApiClient.class)
    public ApiClient apiClient(BamboraProperties bamboraProperties)  {

        ApiClient apiClient = new ApiClient();
        //Setting this to null will make it use the base path
        apiClient.setServerIndex(null);
        apiClient.setBasePath(bamboraProperties.getApiBasePath());
        apiClient.setApiKey(MessageFormat.format("Passcode {0}", bamboraProperties.getEncodedKey()));
        return apiClient;

    }

    @Bean
    public PaymentsApi paymentsApi(BamboraProperties bamboraProperties) {

        ApiClient apiClient = new ApiClient();

        //Setting this to null will make it use the base path
        apiClient.setServerIndex(null);
        apiClient.setBasePath(bamboraProperties.getApiBasePath());
        apiClient.setApiKey(MessageFormat.format("Passcode {0}", bamboraProperties.getPaymentEncodedKey()));

        return new PaymentsApi(apiClient);
    }

    @Bean
    public ProfilesApi profilesApi(ApiClient apiClient) {
        return new ProfilesApi(apiClient);
    }

    @Bean
    public PaymentAdapter paymentAdapter(PaymentsApi paymentsApi, ProfilesApi profilesApi) {
        return new BamboraPaymentAdapter(paymentsApi, profilesApi);
    }

}
