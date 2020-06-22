package ca.bc.gov.open.jag.efilingaccountclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsoAccountServiceImpl implements EfilingAccountService {

    private LookupsAccountFacade lookupsAccountFacade;
    private static final Logger LOGGER = LoggerFactory.getLogger(CsoAccountServiceImpl.class);

    public CsoAccountServiceImpl(LookupsAccountFacade lookupsAccountFacade) {
        this.lookupsAccountFacade = lookupsAccountFacade;
    }

    @Override
    public CsoAccountDetails getAccountDetails(String userGuid) {
        return null;
    }
}
