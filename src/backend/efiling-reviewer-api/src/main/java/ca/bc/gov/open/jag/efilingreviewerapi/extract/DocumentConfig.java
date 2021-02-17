package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.jag.efilingdiligenclientstarter.DiligenProperties;
import ca.bc.gov.open.jag.efilingreviewerapi.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.diligen.DiligenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentConfig {

    private final DiligenProperties diligenProperties;

    public DocumentConfig(DiligenProperties diligenProperties) {
        this.diligenProperties = diligenProperties;
    }

    @Bean
    public DiligenService diligenService() {
        return new DiligenServiceImpl(diligenProperties);
    }

}
