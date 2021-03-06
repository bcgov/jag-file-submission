package ca.bc.gov.open.jag.jagmailit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.open.jag.jagmailit.api.MailSendApi;
import ca.bc.gov.open.jag.jagmailit.api.handler.ApiClient;

@Configuration
@EnableConfigurationProperties(MailSendProperties.class)
public class AutoConfiguration {

    @Bean({"mailItApiClient"})
    @ConditionalOnMissingBean(ApiClient.class)
    public ApiClient apiClient(MailSendProperties mailSendProperties)  {
        ApiClient apiClient = new ApiClient();
        //Setting this to null will make it use the baseUrl instead
        apiClient.setServerIndex(null);
        apiClient.setBasePath(mailSendProperties.getBaseUrl());
        return apiClient;
    }
    
    @Bean
    public MailSendApi mailApi(ApiClient apiClient) {
        return new MailSendApi(apiClient);
    }
    
}
