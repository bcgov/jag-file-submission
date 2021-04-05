package ca.bc.gov.open.jag.efilingapi.court;

import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourtConfig {

    @Bean
    public CourtService courtService(EfilingCourtService efilingCourtService, EfilingSearchService efilingSearchService) {
        return new CourtServiceImpl(efilingCourtService, efilingSearchService);
    }

}
