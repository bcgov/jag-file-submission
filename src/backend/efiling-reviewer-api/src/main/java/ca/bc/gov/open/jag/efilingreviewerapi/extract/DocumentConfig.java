package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.jag.efilingdiligenclientstarter.DiligenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(DiligenProperties.class)
public class DocumentConfig {

    private final DiligenProperties diligenProperties;

    public DocumentConfig(DiligenProperties diligenProperties) {
        this.diligenProperties = diligenProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DiligenService diligenService(DiligenAuthService diligenAuthService, RestTemplate restTemplate) {
        return new DiligenServiceImpl(restTemplate, diligenProperties, diligenAuthService);
    }

}
