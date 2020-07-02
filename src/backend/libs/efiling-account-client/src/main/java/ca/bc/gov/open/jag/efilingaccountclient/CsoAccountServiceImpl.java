package ca.bc.gov.open.jag.efilingaccountclient;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.exception.CSOHasMultipleAccountException;
import ca.bc.gov.open.jag.efilingaccountclient.mappers.AccountDetailsMapper;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bceid.webservices.client.v9.*;
import org.springframework.util.StringUtils;

import java.util.List;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private final AccountFacadeBean accountFacadeBean;
    private final RoleRegistryPortType roleRegistryPortType;
    private final BCeIDServiceSoap bCeIDServiceSoap;
    private final AccountDetailsMapper accountDetailsMapper;

    public CsoAccountServiceImpl(AccountFacadeBean accountFacadeBean,
                                 RoleRegistryPortType roleRegistryPortType,
                                 BCeIDServiceSoap bCeIDServiceSoap,
                                 AccountDetailsMapper accountDetailsMapper) {

        this.accountFacadeBean = accountFacadeBean;
        this.roleRegistryPortType = roleRegistryPortType;
        this.bCeIDServiceSoap = bCeIDServiceSoap;
        this.accountDetailsMapper = accountDetailsMapper;
    }

    @Override
    public AccountDetails getAccountDetails(String userGuid, String bceidAccountType) throws NestedEjbException_Exception {

        if (StringUtils.isEmpty(userGuid)) return null;

        AccountDetails accountDetails = getCsoDetails(userGuid);
        if (null == accountDetails) {
            accountDetails = getBCeidDetails(userGuid, bceidAccountType);
        }

        return accountDetails;
    }

    private AccountDetails getCsoDetails(String userGuid) throws NestedEjbException_Exception  {

        AccountDetails accountDetails = null;

        List<ClientProfile> profiles = accountFacadeBean.findProfiles(userGuid);
        // An account must only one profile associated to proceed
        if (profiles.size() == 1) {
            accountDetails = accountDetailsMapper.toAccountDetails(profiles.get(0), hasFileRole(userGuid));
        }
        else if (profiles.size() > 1) {
            throw new CSOHasMultipleAccountException(profiles.get(0).getClientId().toString());
        }

        return accountDetails;
    }

    private AccountDetails getBCeidDetails(String userGuid, String accountType) {

        AccountDetails accountDetails = null;
        BCeIDAccountTypeCode accountTypeCode = getBCeIDAccountType(accountType);

        if (accountTypeCode != BCeIDAccountTypeCode.VOID) {
            AccountDetailRequest request = new AccountDetailRequest();
            request.setOnlineServiceId("62B2-5550-4376-4DA7");
            request.setRequesterUserGuid(userGuid);
            request.setRequesterAccountTypeCode(accountTypeCode);
            request.setUserGuid(userGuid);
            request.setAccountTypeCode(accountTypeCode);
            AccountDetailResponse response = bCeIDServiceSoap.getAccountDetail(request);
            ResponseCode responseCode = response.getCode();

            if (response.getCode() == ResponseCode.SUCCESS) {
                accountDetails = accountDetailsMapper.toAccountDetails(response.getAccount());
            }
        }

        return accountDetails;
    }

    public boolean hasFileRole(String userGuid) {

        UserRoles userRoles = roleRegistryPortType.getRolesForIdentifier("Courts", "CSO", userGuid, "CAP");
        List<RegisteredRole> roles = userRoles.getRoles();
        return roles != null && roles.stream().anyMatch(r -> r.getCode().equals("FILE"));
    }

    private BCeIDAccountTypeCode getBCeIDAccountType(String bceidAccountType) {
        if (bceidAccountType.equals("Business")) return BCeIDAccountTypeCode.BUSINESS;
        if (bceidAccountType.equals("Individual")) return BCeIDAccountTypeCode.INDIVIDUAL;
        if (bceidAccountType.equals("Verified Individual")) return BCeIDAccountTypeCode.VERIFIED_INDIVIDUAL;
        if (bceidAccountType.equals("EDS")) return BCeIDAccountTypeCode.EDS;
        if (bceidAccountType.equals("Internal")) return BCeIDAccountTypeCode.INTERNAL;
        if (bceidAccountType.equals("LDB")) return BCeIDAccountTypeCode.LDB;
        if (bceidAccountType.equals("THS")) return BCeIDAccountTypeCode.THS;
        return BCeIDAccountTypeCode.VOID;
    }
}
