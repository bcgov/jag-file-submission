package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenAuthService;
import ca.bc.gov.open.jag.efilingdiligenclientstarter.DiligenProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class DocumentConfig {

    private final DiligenProperties diligenProperties;

    public DocumentConfig(DiligenProperties diligenProperties) {
        this.diligenProperties = diligenProperties;
    }

    @Bean
    public DiligenService diligenService(DiligenAuthService diligenAuthService) {
        return new DiligenServiceImpl(new RestTemplate(), diligenProperties, diligenAuthService);
    }

}
