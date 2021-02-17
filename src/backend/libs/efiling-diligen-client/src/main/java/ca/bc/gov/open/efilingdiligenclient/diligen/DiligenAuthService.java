package ca.bc.gov.open.efilingdiligenclient.diligen;

import ca.bc.gov.open.jag.efilingdiligenclient.api.AuthenticationApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface DiligenAuthService {

    String getDiligenJWT(String userName, String password);
}
