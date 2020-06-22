package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.open.jag.efilingaccountclient.DemoAccountServiceImpl;
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

    public AutoConfiguration(CsoAccountProperties csoLookupProperties) {

        this.csoAccountProperties = csoLookupProperties;
    }

    @Bean
    public LookupsAccountFacade eFilingLookupService() {

        LookupsAccountFacade lookupsAccountFacade = null;
        try {

            QName serviceName = new QName("http://ag.gov.bc.ca/csows", "lookups.AccountFacade");
            URL url = new URL(csoAccountProperties.getFilingLookupSoapUri());
            lookupsAccountFacade = new LookupsAccountFacade(url, serviceName);
        } catch(MalformedURLException e) {

            LOGGER.error("Malformed URL exception :" + e.getMessage());
        }
        return lookupsAccountFacade;
    }

//    @Bean
//    @ConditionalOnMissingBean(value = {EfilingAccountService.class})
//    public EfilingAccountService eFilingAccountService() {
//
//        EfilingAccountService eFilingAccountService = new DemoAccountServiceImpl();
//        return eFilingAccountService;
//    }
}
