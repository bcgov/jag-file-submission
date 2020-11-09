package ca.bc.gov.open.jag.ceis;

import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(CeisProperties.class)
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CeisApiClient.class)
    public CeisApiClient ceisApiClient(CeisProperties ceisProperties)  {

        CeisApiClient ceisApiClient = new CeisApiClient();
        ceisApiClient.setBasePath(ceisProperties.getCeisBasePath());
        return ceisApiClient;

    }

    @Bean
    public DefaultApi defaultApi(CeisApiClient ceisApiClient) {
        return new DefaultApi(ceisApiClient);
    }

}
