package ca.bc.gov.open.jag.efilingcsostarter.config;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.ceis.Csows;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.SoapProperties;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import ca.bc.gov.open.jag.efilingcsoclient.*;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({SoapProperties.class, SpringCsoProperties.class})
public class AutoConfiguration {

    private final SoapProperties soapProperties;

    private final CsoProperties csoProperties;

    public AutoConfiguration(SoapProperties soapProperties, SpringCsoProperties csoProperties) {
        this.soapProperties = soapProperties;
        this.csoProperties = csoProperties;
    }

    @Bean
    public AccountFacadeBean accountFacadeBean() {
        return SoapUtils.getPort(Clients.ACCOUNT, AccountFacadeBean.class, soapProperties, csoProperties.isDebugEnabled());
    }

    @Bean
    public RoleRegistryPortType roleRegistryPortType() {
       return SoapUtils.getPort(Clients.ROLE, RoleRegistryPortType.class, soapProperties, csoProperties.isDebugEnabled());
    }

    @Bean
    public FilingStatusFacadeBean filingStatusFacadeBean() { return SoapUtils.getPort(Clients.STATUS, FilingStatusFacadeBean.class, soapProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public LookupFacadeBean lookupFacadeBean() { return SoapUtils.getPort(Clients.LOOKUP, LookupFacadeBean.class, soapProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public Csows csows() { return SoapUtils.getPort(Clients.CSOWS, Csows.class, soapProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public FilingFacadeBean filingFacadeBean() { return SoapUtils.getPort(Clients.FILING, FilingFacadeBean.class, soapProperties, csoProperties.isDebugEnabled()); }

    @Bean
    public ServiceFacadeBean serviceFacadeBean() { return SoapUtils.getPort(Clients.SERVICE, ServiceFacadeBean.class, soapProperties, csoProperties.isDebugEnabled()); }

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
    public EfilingAccountService efilingAccountService(AccountFacadeBean accountFacadeBean,
                                                       RoleRegistryPortType roleRegistryPortType,
                                                       AccountDetailsMapper accountDetailsMapper) {
        return new CsoAccountServiceImpl(accountFacadeBean, roleRegistryPortType, accountDetailsMapper);
    }

    @Bean
    public EfilingDocumentService efilingDocumentService(FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoDocumentServiceImpl(filingStatusFacadeBean);
    }

    @Bean
    public EfilingLookupService efilingLookupService(LookupFacadeBean lookupFacadeBean) {
        return new CsoLookupServiceImpl(lookupFacadeBean);
    }

    @Bean
    public CsoCourtServiceImpl efilingCourtService(Csows csows, FilingStatusFacadeBean filingStatusFacadeBean) {
        return new CsoCourtServiceImpl(csows, filingStatusFacadeBean);
    }

    @Bean
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




}
