package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SeedAccount {

    public static final UUID ACCOUNT_WITH_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fa");
    public static final UUID ACCOUNT_WITHOUT_EFILING_ROLE = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fb");

    private static final Logger logger = LoggerFactory.getLogger(SeedAccount.class);

    private final EfilingAccountService efilingAccountService;

    public SeedAccount(Environment environment, EfilingAccountService efilingAccountService){

        this.efilingAccountService = efilingAccountService;

    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
    }

    public void init() {

        CreateAccountRequest createAccount1 = CreateAccountRequest
                .builder()
                .universalId(ACCOUNT_WITH_EFILING_ROLE)
                .firstName("temp")
                .lastName("temp")
                .middleName("temp")
                .email("temp")
                .create();

        AccountDetails account1 = efilingAccountService.createAccount(createAccount1);

        logger.info("Account 1 created {}", account1.getUniversalId());

        CreateAccountRequest createAccount2 = CreateAccountRequest
                .builder()
                .universalId(ACCOUNT_WITHOUT_EFILING_ROLE)
                .firstName("temp")
                .lastName("temp")
                .middleName("temp")
                .email("temp")
                .create();

        AccountDetails account2 = efilingAccountService.createAccount(createAccount2);

        logger.info("Account 2 created {}", account2.getUniversalId());

    }

}
