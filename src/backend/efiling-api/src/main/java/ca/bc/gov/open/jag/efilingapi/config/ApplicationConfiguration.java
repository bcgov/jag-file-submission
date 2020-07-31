package ca.bc.gov.open.jag.efilingapi.config;

import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Application Configuration
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * Configures Jeager tracer.
     */
    @Bean
    public Tracer jaegerTracer() {
        return new io.jaegertracing.Configuration("spring-boot")
                .getTracer();
    }

}
