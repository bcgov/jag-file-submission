package ca.bc.gov.open.jag.efilingaccountclient.config;

import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
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

    private final CsoAccountProperties csoAccountProperties;

    public AutoConfiguration(CsoAccountProperties csoAccountProperties) {

        this.csoAccountProperties = csoAccountProperties;
    }

    public AccountFacade accountFacade() throws MalformedURLException {
        QName serviceName = new QName("http://accounts.csows.ag.gov.bc.ca/", "AccountFacade");
        URL url = new URL(csoAccountProperties.getFilingAccountSoapUri());
        return new AccountFacade(url, serviceName);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService() throws MalformedURLException {
        return new CsoAccountServiceImpl(accountFacade());
    }
}
