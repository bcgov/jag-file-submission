package ca.bc.gov.open.jag.efilingapi.config;

import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Application Configuration
 */
@Configuration
public class ApplicationConfiguration {

//    /**
//     * Configures CORS for enabling javascript applications to connect.
//     */
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
//            }
//        };
//    }

    /**
     * Configures Jeager tracer.
     */
    @Bean
    public Tracer jaegerTracer() {
        return new io.jaegertracing.Configuration("spring-boot")
                .getTracer();
    }

}
