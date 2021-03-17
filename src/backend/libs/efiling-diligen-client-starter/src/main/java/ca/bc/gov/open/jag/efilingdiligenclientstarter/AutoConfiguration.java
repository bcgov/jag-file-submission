package ca.bc.gov.open.jag.efilingdiligenclientstarter;

import ca.bc.gov.open.efilingdiligenclient.diligen.*;
import ca.bc.gov.open.efilingdiligenclient.diligen.mapper.DiligenDocumentDetailsMapperImpl;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessor;
import ca.bc.gov.open.efilingdiligenclient.diligen.processor.FieldProcessorImpl;
import ca.bc.gov.open.jag.efilingdiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.DocumentsApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.HealthCheckApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Diligen Client configuration
 */
@Configuration
@EnableConfigurationProperties(DiligenProperties.class)
public class AutoConfiguration {

    private final DiligenProperties diligenProperties;

    public AutoConfiguration(DiligenProperties diligenProperties) {
        this.diligenProperties = diligenProperties;
    }

    /**
     * @return ApiClient - a configured api client for diligen
     */
    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();

        if(StringUtils.isBlank(diligenProperties.getBasePath()))
            throw new DiligenConfigurationException("Diligen base path is required");

        apiClient.setBasePath(diligenProperties.getBasePath());

        return apiClient;
    }

    /**
     * @param apiClient - a diligen api client
     * @return HealthCheckApi - diligen healthCheckApi client
     */
    @Bean
    public HealthCheckApi healthCheckApi(ApiClient apiClient) {
        return new HealthCheckApi(apiClient);
    }

    /**
     * @param healthCheckApi - diligen healthcheck api
     * @return Diligen implementation of HealthIndicator
     */
    @Bean
    @ConditionalOnProperty(prefix = "jag.efiling.diligen", name = "health.enabled", havingValue = "true")
    public HealthIndicator diligenHealthIndicator(HealthCheckApi healthCheckApi) {
        return new DiligenHealthIndicator(healthCheckApi);
    }

    @Bean
    public DiligenAuthService diligenAuthService(ApiClient apiClient) {
        AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);
        return new DiligenAuthServiceImpl(authenticationApi);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DocumentsApi documentsApi(ApiClient apiClient) {
        return new DocumentsApi(apiClient);
    }

    @Bean
    public DiligenService diligenService(DiligenAuthService diligenAuthService, RestTemplate restTemplate, DocumentsApi documentsApi) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return new DiligenServiceImpl(restTemplate, diligenProperties, diligenAuthService, objectMapper, documentsApi, new DiligenDocumentDetailsMapperImpl());
    }

    @Bean
    public FieldProcessor fieldProcessor() {
        return new FieldProcessorImpl();
    }

}
