package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.exception.CSOHasMultipleAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private AccountFacadeBean accountFacadeBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(CsoAccountServiceImpl.class);

    public CsoAccountServiceImpl(AccountFacadeBean accountFacadeBean) {

        this.accountFacadeBean = accountFacadeBean;

    }

    @Override
    public CsoAccountDetails getAccountDetails(String userGuid) {

        CsoAccountDetails csoAccountDetails = null;
        if (StringUtils.isEmpty(userGuid)) return csoAccountDetails;

        try {



            List<ClientProfile> profiles = accountFacadeBean.findProfiles(userGuid);
            //An account must only one profile associated to proceed
            if (profiles.size() == 1) {
                ClientProfile profile = profiles.get(0);
                csoAccountDetails = new CsoAccountDetails(profile.getAccountId(), profile.getClientId());
                csoAccountDetails.addRole("efiling");
            } else if (profiles.size() > 1) {
                throw new CSOHasMultipleAccountException(profiles.get(0).getClientId().toString());
            }

        }
        catch(NestedEjbException_Exception e) {

            LOGGER.error("Error calling findProfiles: ", e);
        }

        return csoAccountDetails;
     }
}
