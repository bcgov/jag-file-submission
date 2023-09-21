package ca.bc.gov.open.jag.efiling.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;

@Component
public class SeedAccount {

    private static final Logger logger = LoggerFactory.getLogger(SeedAccount.class);

    private final EfilingAccountService efilingAccountService;

    public SeedAccount(EfilingAccountService efilingAccountService){

        this.efilingAccountService = efilingAccountService;

    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }

    private void init() {

        CreateAccountRequest createAccount1 = CreateAccountRequest
                .builder()
                .universalId(Keys.ACCOUNT_WITH_EFILING_ROLE)
                .firstName("Vivian")
                .lastName("Brown")
                .email("vbrown@paintit.com")
                .create();

        AccountDetails account1 = efilingAccountService.createAccount(createAccount1);

        logger.info("Account 1 created {}", account1.getUniversalId());

        CreateAccountRequest createAccount2 = CreateAccountRequest
                .builder()
                .universalId(Keys.ACCOUNT_WITHOUT_EFILING_ROLE)
                .firstName("Lynda")
                .lastName("Ridge")
                .email("lridge@paintit.com")
                .create();

        AccountDetails account2 = efilingAccountService.createAccount(createAccount2);

        logger.info("Account 2 created {}", account2.getUniversalId());

    }

}
