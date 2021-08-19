package ca.bc.gov.open.jag.efilingapi.filingpackage;


import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.ActionRequiredDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilingPackageConfig {
    @Bean
    public FilingPackageService filePackageService(EfilingReviewService efilingReviewService, AccountService accountService) {
        return new FilingPackageServiceImpl(efilingReviewService, accountService, new FilingPackageMapperImpl(), new ActionRequiredDetailsMapperImpl());
    }

}
