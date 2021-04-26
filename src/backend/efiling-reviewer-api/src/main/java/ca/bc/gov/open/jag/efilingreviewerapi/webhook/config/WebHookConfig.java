package ca.bc.gov.open.jag.efilingreviewerapi.document.service.config;

import ca.bc.gov.open.jag.efilingreviewerapi.document.service.WebHookService;
import ca.bc.gov.open.jag.efilingreviewerapi.document.service.WebHookServiceImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.document.service.properties.WebHookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({WebHookProperties.class})
public class WebHookConfig {

    private final WebHookProperties webHookProperties;

    public WebHookConfig(WebHookProperties webHookProperties) {
        this.webHookProperties = webHookProperties;
    }

    @Bean
    public WebHookService webHookService(RestTemplate restTemplate) {
        return new WebHookServiceImpl(restTemplate, webHookProperties);
    }

}
