package ca.bc.gov.open.jag.efilingaccountclient.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingaccountclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingaccountclient.mappers.AccountDetailsMapperImpl;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import ca.bceid.webservices.client.v9.BCeIDServiceSoap;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@EnableConfigurationProperties(SoapProperties.class)
public class AutoConfiguration {

    private final SoapProperties soapProperties;


    public AutoConfiguration(SoapProperties soapProperties) {
        this.soapProperties = soapProperties;
    }

    @Bean
    public AccountFacadeBean accountFacadeBean() {
        return getPort(Clients.ACCOUNT, AccountFacadeBean.class);
    }

    @Bean
    public RoleRegistryPortType roleRegistryPortType() {
       return getPort(Clients.ROLE, RoleRegistryPortType.class);
    }

    @Bean
    public BCeIDServiceSoap bCeIDServiceSoap() { return getPort(Clients.BCEID, BCeIDServiceSoap.class); }

    @Bean
    public AccountDetailsMapper accountDetailsMapper() {
        return new AccountDetailsMapperImpl();
    }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean,
                                                       RoleRegistryPortType roleRegistryPortType,
                                                       BCeIDServiceSoap bCeIDServiceSoap,
                                                       AccountDetailsMapper accountDetailsMapper) {
        return new CsoAccountServiceImpl(accountFacadeBean, roleRegistryPortType,
                                         bCeIDServiceSoap, accountDetailsMapper);
    }

    public <T> T getPort(Clients clients, Class<T> type) {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(type);
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(clients);
        jaxWsProxyFactoryBean.setAddress(efilingSoapClientProperties.getUri());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getUserName()))
            jaxWsProxyFactoryBean.setUsername(efilingSoapClientProperties.getUserName());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getPassword()))
            jaxWsProxyFactoryBean.setPassword(efilingSoapClientProperties.getPassword());
        return type.cast(jaxWsProxyFactoryBean.create());
    }

}
