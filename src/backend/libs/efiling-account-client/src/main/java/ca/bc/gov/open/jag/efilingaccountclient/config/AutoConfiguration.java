package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.ag.csows.accounts.AccountFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Bean
    public AccountFacade eFilingLookupService() {

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

//    @Bean
//    @ConditionalOnMissingBean(value = {EfilingAccountService.class})
//    public EfilingAccountService eFilingAccountService() {
//
//        EfilingAccountService eFilingAccountService = new DemoAccountServiceImpl();
//        return eFilingAccountService;
//    }
}
