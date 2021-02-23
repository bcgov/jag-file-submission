package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {

    @Bean
    public ExtractStore extractStore() {
        return new CacheExtractStore();
    }

}
