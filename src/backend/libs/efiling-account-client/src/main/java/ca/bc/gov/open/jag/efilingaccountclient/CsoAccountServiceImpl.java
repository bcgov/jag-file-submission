package ca.bc.gov.open.jag.efilingaccountclient;

import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RegisteredRole;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.RoleRegistryPortType;
import brooks.roleregistry_source_roleregistry_ws_provider.roleregistry.UserRoles;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.exception.CSOHasMultipleAccountException;
import org.springframework.util.StringUtils;

import java.util.List;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private AccountFacadeBean accountFacadeBean;
    private RoleRegistryPortType roleRegistryPortType;

    public CsoAccountServiceImpl(AccountFacadeBean accountFacadeBean, RoleRegistryPortType roleRegistryPortType) {

        this.accountFacadeBean = accountFacadeBean;
        this.roleRegistryPortType = roleRegistryPortType;
    }

    @Override
    public CsoAccountDetails getAccountDetails(String userGuid) throws NestedEjbException_Exception {

        if (StringUtils.isEmpty(userGuid)) return null;

        CsoAccountDetails csoAccountDetails = null;
        List<ClientProfile> profiles = accountFacadeBean.findProfiles(userGuid);
        //An account must only one profile associated to proceed
        if (profiles.size() == 1) {
            ClientProfile profile = profiles.get(0);
            csoAccountDetails = new CsoAccountDetails(profile.getAccountId(), profile.getClientId(), HasFileRole(userGuid));
        }
        else if (profiles.size() > 1) {
            throw new CSOHasMultipleAccountException(profiles.get(0).getClientId().toString());
        }

        return csoAccountDetails;
    }

    public boolean HasFileRole(String userGuid) {

        UserRoles userRoles = roleRegistryPortType.getRolesForIdentifier("Courts", "CSO", userGuid, "CAP");
        List<RegisteredRole> roles = userRoles.getRoles();
        return roles != null && roles.stream().anyMatch(r -> r.getCode().equals("FILE"));
    }
}
