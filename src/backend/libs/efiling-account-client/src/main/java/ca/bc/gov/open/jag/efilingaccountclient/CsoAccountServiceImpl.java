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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private final AccountFacadeBean accountFacadeBean;
    private final RoleRegistryPortType roleRegistryPortType;
    private final BCeIDServiceSoap bCeIDServiceSoap;
    private final AccountDetailsMapper accountDetailsMapper;

    private static final Map<String, BCeIDAccountTypeCode> accountTypeLookup;
    static {
        Map<String, BCeIDAccountTypeCode> tempMap = new HashMap<String, BCeIDAccountTypeCode>();
        tempMap.put("business", BCeIDAccountTypeCode.BUSINESS);
        tempMap.put("verified individual", BCeIDAccountTypeCode.INDIVIDUAL);
        tempMap.put("eds", BCeIDAccountTypeCode.EDS);
        tempMap.put("internal", BCeIDAccountTypeCode.INTERNAL);
        tempMap.put("ldb", BCeIDAccountTypeCode.LDB);
        tempMap.put("ths", BCeIDAccountTypeCode.THS);

        accountTypeLookup = Collections.unmodifiableMap(tempMap);;
    }

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
        String lookUp = bceidAccountType.toLowerCase();
        BCeIDAccountTypeCode code = accountTypeLookup.get(lookUp);
        return code == null? BCeIDAccountTypeCode.VOID : code;
    }
}
