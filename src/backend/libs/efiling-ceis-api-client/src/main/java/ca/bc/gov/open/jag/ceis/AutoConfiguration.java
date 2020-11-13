package ca.bc.gov.open.jag.ceis;

import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingceisapiclient.api.handler.ApiClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
@EnableConfigurationProperties(CeisProperties.class)
public class AutoConfiguration {

    @Bean({"ceisApiClient"})
    @ConditionalOnMissingBean(ApiClient.class)
    public ApiClient ceisApiClient(CeisProperties ceisProperties)  {

        ApiClient apiClient = new ApiClient();
        //Setting this to null will make it use the base path
        apiClient.setServerIndex(null);
        apiClient.setBasePath(ceisProperties.getCeisBasePath());
        apiClient.setUsername(ceisProperties.getCeisUsername());
        apiClient.setPassword(ceisProperties.getCeisPassword());
        return apiClient;

    }

    @Bean
    public DefaultApi defaultApi(@Qualifier("ceisApiClient") final ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }

}
