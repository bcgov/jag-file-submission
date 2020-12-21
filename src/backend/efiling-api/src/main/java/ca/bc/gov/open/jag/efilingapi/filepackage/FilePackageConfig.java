package ca.bc.gov.open.jag.efilingapi.filepackage;


import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.filepackage.service.FilePackageService;
import ca.bc.gov.open.jag.efilingapi.filepackage.service.FilePackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import org.springframework.context.annotation.Bean;

public class FilePackageConfig {
    @Bean
    public FilePackageService filePackageService(EfilingStatusService efilingStatusService, AccountService accountService) {
        return new FilePackageServiceImpl(efilingStatusService, accountService);
    }

}
