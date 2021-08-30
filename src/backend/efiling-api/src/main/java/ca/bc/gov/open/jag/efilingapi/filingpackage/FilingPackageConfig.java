package ca.bc.gov.open.jag.efilingapi.filingpackage;


import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.ActionRequiredDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.properties.ParentProperties;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ParentProperties.class})
public class FilingPackageConfig {

    private final ParentProperties parentProperties;

    public FilingPackageConfig(ParentProperties parentProperties) {
        this.parentProperties = parentProperties;
    }

    @Bean
    public FilingPackageService filePackageService(EfilingReviewService efilingReviewService, AccountService accountService) {
        return new FilingPackageServiceImpl(efilingReviewService, accountService, new FilingPackageMapperImpl(), new ActionRequiredDetailsMapperImpl(), parentProperties);
    }

}
