package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.accounts.AccountFacade;
import ca.bc.gov.ag.csows.accounts.AccountFacadeBean;
import ca.bc.gov.ag.csows.accounts.ClientProfile;
import ca.bc.gov.ag.csows.accounts.NestedEjbException_Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private AccountFacade accountFacade;
    private static final Logger LOGGER = LoggerFactory.getLogger(CsoAccountServiceImpl.class);

    public CsoAccountServiceImpl(AccountFacade accountFacade) {

        this.accountFacade = accountFacade;
    }

    @Override
    public CsoAccountDetails getAccountDetails(String userGuid) {

        CsoAccountDetails csoAccountDetails = null;
        if (StringUtils.isEmpty(userGuid)) return csoAccountDetails;

        try {

            AccountFacadeBean accountFacadeBeanPort = accountFacade.getAccountFacadeBeanPort();
            List<ClientProfile> profiles = accountFacadeBeanPort.findProfiles(userGuid);

            if (profiles.size() == 1) {
                ClientProfile profile = profiles.get(0);
                csoAccountDetails = new CsoAccountDetails(profile.getAccountId(), profile.getClientId());
            }

        }
        catch(NestedEjbException_Exception e) {

            LOGGER.error("Error calling findProfiles: ", e);
        }

        return csoAccountDetails;
     }
}
