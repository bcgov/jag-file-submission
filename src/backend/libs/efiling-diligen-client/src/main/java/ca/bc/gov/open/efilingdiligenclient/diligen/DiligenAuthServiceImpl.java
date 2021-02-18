package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.efilingdiligenclient.exception.DiligenAuthenticationException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.AuthenticationApi;
import ca.bc.gov.open.jag.efilingdiligenclient.api.handler.ApiException;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineObject;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.InlineResponse2001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiligenAuthServiceImpl implements DiligenAuthService {
    Logger logger = LoggerFactory.getLogger(DiligenAuthServiceImpl.class);

    private final AuthenticationApi authenticationApi;

    public DiligenAuthServiceImpl(AuthenticationApi authenticationApi) {
        this.authenticationApi = authenticationApi;
    }

    @Override
    public String getDiligenJWT(String userName, String password) {
        InlineObject loginParams = new InlineObject();
        loginParams.setEmail(userName);
        loginParams.setPassword(password);
        try {

            InlineResponse2001 result = authenticationApi.apiLoginPost(loginParams);

            if (result.getData() == null) throw new DiligenAuthenticationException("No login data");

            logger.info("diligen login complete");

            return result.getData().getJwt();

        } catch (ApiException e) {
            throw new DiligenAuthenticationException(e.getMessage());
        }
    }
}
