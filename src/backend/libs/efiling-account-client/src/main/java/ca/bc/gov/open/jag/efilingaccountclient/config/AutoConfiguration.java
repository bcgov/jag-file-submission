package ca.bc.gov.open.jag.efilingaccountclient.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.open.jag.efilingaccountclient.CsoAccountServiceImpl;
import ca.bc.gov.open.jag.efilingaccountclient.EfilingAccountService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@EnableConfigurationProperties(CsoAccountProperties.class)
public class AutoConfiguration {

    private final CsoAccountProperties csoAccountProperties;

    public AutoConfiguration(CsoAccountProperties csoAccountProperties) {

        this.csoAccountProperties = csoAccountProperties;
    }

    @Bean
    public AccountFacadeBean accountFacadeBean() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
                new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(AccountFacadeBean.class);
        jaxWsProxyFactoryBean.setAddress(csoAccountProperties.getFilingAccountSoapUri());
        jaxWsProxyFactoryBean.setUsername(csoAccountProperties.getUserName());
        jaxWsProxyFactoryBean.setPassword(csoAccountProperties.getPassword());
        return (AccountFacadeBean) jaxWsProxyFactoryBean.create();
    }

    @Bean
    public RoleRegistryPortType roleRegistryPortType() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBeanTest = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBeanTest.setServiceClass(RoleRegistryPortType.class);
        jaxWsProxyFactoryBeanTest.setAddress(csoAccountProperties.getFilingRoleSoapUri());
        jaxWsProxyFactoryBeanTest.setUsername(csoAccountProperties.getUserName());
        jaxWsProxyFactoryBeanTest.setPassword(csoAccountProperties.getPassword());
        return (RoleRegistryPortType) jaxWsProxyFactoryBeanTest.create();
    }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean, RoleRegistryPortType roleRegistryPortType) {
        return new CsoAccountServiceImpl(accountFacadeBean, roleRegistryPortType);
    }


}
