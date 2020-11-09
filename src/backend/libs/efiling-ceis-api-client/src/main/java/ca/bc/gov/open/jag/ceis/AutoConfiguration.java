package ca.bc.gov.open.jag.ceis;

import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(CeisProperties.class)
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApiClient.class)
    public ApiClient apiClient(CeisProperties ceisProperties)  {

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(ceisProperties.getCeisBasePath());
        return new ApiClient();

    }

    @Bean
    public DefaultApi paymentsApi(ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }

}
