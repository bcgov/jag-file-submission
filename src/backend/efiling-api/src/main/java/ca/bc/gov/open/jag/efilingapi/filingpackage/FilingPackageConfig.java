package ca.bc.gov.open.jag.efilingapi.filingpackage;


import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import org.springframework.context.annotation.Bean;

public class FilingPackageConfig {
    @Bean
    public FilingPackageService filePackageService(EfilingStatusService efilingStatusService, AccountService accountService) {
        return new FilingPackageServiceImpl(efilingStatusService, accountService, new FilingPackageMapperImpl());
    }

}
