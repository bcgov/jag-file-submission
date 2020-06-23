package ca.bc.gov.open.jag.efilingapi.config;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;



public class JaegerConfig {
    @Bean
    public Tracer jaegerTracer() {
        return new Configuration("spring-boot")
                .getTracer();
    }

}
