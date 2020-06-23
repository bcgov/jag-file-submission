package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties(CsoAccountProperties.class)
public class AutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfiguration.class);
    private final CsoAccountProperties csoAccountProperties;

    public AutoConfiguration(CsoAccountProperties csoAccountProperties) {

        this.csoAccountProperties = csoAccountProperties;
    }


    public AccountFacade accountFacade() {

        AccountFacade accountFacade = null;
        try {

            QName serviceName = new QName("http://accounts.csows.ag.gov.bc.ca/", "AccountFacade");
            URL url = new URL(csoAccountProperties.getFilingAccountSoapUri());
            accountFacade = new AccountFacade(url, serviceName);
        } catch(MalformedURLException e) {

            LOGGER.error("Malformed URL exception :" + e.getMessage());
        }
        return accountFacade;
    }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService() {

        return new CsoAccountServiceImpl(accountFacade());
    }
}
