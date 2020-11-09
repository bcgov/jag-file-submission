package ca.bc.gov.open.jag.efilingapi.courts;


import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingceisapiclient.api.DefaultApi;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourtsConfiguration {

    @Bean
    public CeisLookupAdapter ceisLookupAdapter(DefaultApi defaultApi, CourtLocationMapper courtLocationMapper) {
        return new CeisLookupAdapter(defaultApi, courtLocationMapper);
    }

}
