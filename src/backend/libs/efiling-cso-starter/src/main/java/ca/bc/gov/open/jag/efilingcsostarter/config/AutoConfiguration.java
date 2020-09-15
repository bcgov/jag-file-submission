package ca.bc.gov.open.jag.efilingcsostarter.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.ceis.Csows;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.jag.efilingcsostarter.*;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({SoapProperties.class, CsoProperties.class})
public class AutoConfiguration {

    private final SoapProperties soapProperties;

    private final CsoProperties csoProperties;

    public AutoConfiguration(SoapProperties soapProperties, CsoProperties csoProperties) {
        this.soapProperties = soapProperties;
        this.csoProperties = csoProperties;
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
    public FilingStatusFacadeBean filingStatusFacadeBean() { return getPort(Clients.STATUS, FilingStatusFacadeBean.class); }

    @Bean
    public LookupFacadeBean lookupFacadeBean() { return getPort(Clients.LOOKUP, LookupFacadeBean.class); }

    @Bean
    public Csows csows() { return getPort(Clients.CSOWS, Csows.class); }

    @Bean
    public FilingFacadeBean filingFacadeBean() { return getPort(Clients.FILING, FilingFacadeBean.class); }

    @Bean
    public ServiceFacadeBean serviceFacadeBean() { return getPort(Clients.SERVICE, ServiceFacadeBean.class); }

    @Bean
    public AccountDetailsMapper accountDetailsMapper() {
        return new AccountDetailsMapperImpl();
    }

    @Bean
    public ServiceMapper serviceMapper() {
        return new ServiceMapperImpl();
    }

    @Bean
    public FilingPackageMapper filingPackageMapper() {
        return new FilingPackageMapperImpl();
    }

    @Bean
    public FinancialTransactionMapper financialTransactionMapper() { return new FinancialTransactionMapperImpl(); }

    @Bean
    public DocumentMapper documentMapper() {
        return new DocumentMapperImpl();
    }

    @Bean
    public CsoPartyMapper csoPartyMapper() { return new CsoPartyMapperImpl(); }

    @Bean
    public PackageAuthorityMapper packageAuthorityMapper() { return new PackageAuthorityMapperImpl(); }

    @Bean
    @ConditionalOnMissingBean({EfilingAccountService.class})
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean,
                                                       RoleRegistryPortType roleRegistryPortType,
                                                       AccountDetailsMapper accountDetailsMapper) {
        return new CsoAccountServiceImpl(accountFacadeBean, roleRegistryPortType, accountDetailsMapper);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingDocumentService.class})
    public EfilingDocumentService efilingDocumentService(FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoDocumentServiceImpl(filingStatusFacadeBean);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingLookupService.class})
    public EfilingLookupService efilingLookupService(LookupFacadeBean lookupFacadeBean) {
        return new CsoLookupServiceImpl(lookupFacadeBean);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingCourtService.class})
    public CsoCourtServiceImpl efilingCourtService(Csows csows, FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoCourtServiceImpl(csows, filingStatusFacadeBean);
    }

    @Bean
    @ConditionalOnMissingBean({EfilingSubmissionService.class})
    public EfilingSubmissionService efilingSubmissionService(FilingFacadeBean filingFacadeBean,
                                                             ServiceFacadeBean serviceFacadeBean,
                                                             ServiceMapper serviceMapper,
                                                             FilingPackageMapper filingPackageMapper,
                                                             FinancialTransactionMapper financialTransactionMapper,
                                                             DocumentMapper documentMapper,
                                                             CsoPartyMapper csoPartyMapper,
                                                             PackageAuthorityMapper packageAuthorityMapper) {

        return new CsoSubmissionServiceImpl(
                filingFacadeBean,
                serviceFacadeBean,
                serviceMapper,
                filingPackageMapper,
                financialTransactionMapper,
                csoProperties,
                documentMapper,
                csoPartyMapper,
                packageAuthorityMapper); }


    public <T> T getPort(Clients clients, Class<T> type) {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(type);
        EfilingSoapClientProperties efilingSoapClientProperties = soapProperties.findByEnum(clients);
        jaxWsProxyFactoryBean.setAddress(efilingSoapClientProperties.getUri());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getUserName()))
            jaxWsProxyFactoryBean.setUsername(efilingSoapClientProperties.getUserName());
        if(StringUtils.isNotBlank(efilingSoapClientProperties.getPassword()))
            jaxWsProxyFactoryBean.setPassword(efilingSoapClientProperties.getPassword());

        if(csoProperties.isDebugEnabled()) {
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            loggingInInterceptor.setPrettyLogging(true);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            loggingOutInterceptor.setPrettyLogging(true);
            jaxWsProxyFactoryBean.getOutInterceptors().add(0, loggingOutInterceptor);
            jaxWsProxyFactoryBean.getInInterceptors().add(0, loggingInInterceptor);
        }

        return type.cast(jaxWsProxyFactoryBean.create());
    }

}
