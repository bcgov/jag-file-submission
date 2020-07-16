package ca.bc.gov.open.jag.efilingapi.document;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentConfiguration {

    @Bean
    public DocumentStore documentStore() {
        return new DocumentStoreImpl();
    }

}
