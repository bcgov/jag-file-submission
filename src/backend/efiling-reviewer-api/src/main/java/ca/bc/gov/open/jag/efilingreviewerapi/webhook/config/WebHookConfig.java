package ca.bc.gov.open.jag.efilingreviewerapi.webhook.config;

import ca.bc.gov.open.jag.efilingreviewerapi.webhook.properties.WebHookProperties;
import ca.bc.gov.open.jag.efilingreviewerapi.webhook.WebHookService;
import ca.bc.gov.open.jag.efilingreviewerapi.webhook.WebHookServiceImpl;
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
