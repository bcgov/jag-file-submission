package ca.bc.gov.open.jag.bambora;

import ca.bc.gov.open.jag.efilingbamboraapiclient.api.PaymentsApi;
import ca.bc.gov.open.jag.efilingbamboraapiclient.api.handler.ApiClient;
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
        apiClient.setApiKey(MessageFormat.format("Passcode {0}", bamboraProperties.getPasscode()));
        return apiClient;

    }

    @Bean
    public PaymentsApi paymentsApi(ApiClient apiClient) {
        return new PaymentsApi(apiClient);
    }

}
