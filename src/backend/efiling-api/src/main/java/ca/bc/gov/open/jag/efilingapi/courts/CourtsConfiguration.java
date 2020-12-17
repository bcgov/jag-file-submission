package ca.bc.gov.open.jag.efilingapi.courts;


import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapper;
import ca.bc.gov.open.jag.efilingapi.courts.mappers.CourtLocationMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourtsConfiguration {

    @Bean
    public CourtLocationMapper courtLocationMapper() {
        return new CourtLocationMapperImpl();
    }

}
