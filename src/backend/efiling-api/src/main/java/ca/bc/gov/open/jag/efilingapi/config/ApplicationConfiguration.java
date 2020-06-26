package ca.bc.gov.open.jag.efilingapi.config;

import ca.bc.gov.open.jag.efilingaccountclient.DemoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilinglookupclient.DemoLookupServiceImpl;
import ca.bc.gov.open.jag.efilinglookupclient.EfilingLookupService;
import io.opentracing.Tracer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Application Configuration
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {


    /**
     * If the application demo mode is turned on, use the DemoAccountServiceImpl
     */
    @Bean
    @ConditionalOnProperty(name = "jag.efiling.global.demo", havingValue = "true")
    public EfilingAccountService efilingAccountService() {
        return new DemoAccountServiceImpl();
    }

    /**
     * If the application demo mode is turned on, use the DemoLookupServiceImpl
     */
    @Bean
    @ConditionalOnProperty(name = "jag.efiling.global.demo", havingValue = "true")
    public EfilingLookupService efilingLookupService() {
        return new DemoLookupServiceImpl();
    }

    /**
     * Configures CORS for enabling javascript applications to connect.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
            }
        };
    }

    /**
     * Configures Jeager tracer.
     */
    @Bean
    public Tracer jaegerTracer() {
        return new io.jaegertracing.Configuration("spring-boot")
                .getTracer();
    }

}
