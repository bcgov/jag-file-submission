package ca.bc.gov.open.jag.efilingcourtdocumentapiclient.config;

import ca.bc.gov.open.jag.efilingcourtdocumentapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingcourtdocumentapiclient.api.handler.ApiClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CourtDocumentProperties.class)
public class AutoConfiguration {

    @Bean({"courtDocumentApiClient"})
    public ApiClient courtDocumentApiClient(CourtDocumentProperties courtDocumentProperties)  {

        ApiClient apiClient = new ApiClient();
        //Setting this to null will make it use the base path
        apiClient.setServerIndex(null);
        apiClient.setBasePath(courtDocumentProperties.getBasePath());
        if(!StringUtils.isEmpty(courtDocumentProperties.getUsername())) {
            apiClient.setUsername(courtDocumentProperties.getUsername());
            apiClient.setPassword(courtDocumentProperties.getPassword());
        }
        return apiClient;

    }

    @Bean
    public DefaultApi defaultApi(@Qualifier("courtDocumentApiClient") final ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }

}
