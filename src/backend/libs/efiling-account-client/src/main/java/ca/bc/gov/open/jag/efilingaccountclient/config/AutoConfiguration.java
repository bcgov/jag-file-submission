package ca.bc.gov.open.jag.efilingaccountclient.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
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
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
                new JaxWsProxyFactoryBean();
        EfilingSoapClientProperties csoAccountProperties = soapProperties.findByEnum(Clients.ACCOUNT);
        jaxWsProxyFactoryBean.setServiceClass(AccountFacadeBean.class);
        jaxWsProxyFactoryBean.setAddress(csoAccountProperties.getUri());
        jaxWsProxyFactoryBean.setUsername(csoAccountProperties.getUserName());
        jaxWsProxyFactoryBean.setPassword(csoAccountProperties.getPassword());
        return (AccountFacadeBean) jaxWsProxyFactoryBean.create();
    }

    @Bean
    public RoleRegistryPortType roleRegistryPortType() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(RoleRegistryPortType.class);
        EfilingSoapClientProperties csoRoleProperties = soapProperties.findByEnum(Clients.ROLE);
        jaxWsProxyFactoryBean.setAddress(csoRoleProperties.getUri());
       if(StringUtils.isNotBlank(csoRoleProperties.getUserName()))  jaxWsProxyFactoryBean.setUsername(csoRoleProperties.getUserName());
        if(StringUtils.isNotBlank(csoRoleProperties.getPassword())) jaxWsProxyFactoryBean.setPassword(csoRoleProperties.getPassword());
        return (RoleRegistryPortType) jaxWsProxyFactoryBean.create();
    }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean) {
        return new CsoAccountServiceImpl(accountFacadeBean);
    }


}
