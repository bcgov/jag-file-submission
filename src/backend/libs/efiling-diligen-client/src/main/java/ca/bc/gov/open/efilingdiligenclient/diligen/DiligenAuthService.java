package ca.bc.gov.open.efilingdiligenclient.diligen;

public interface DiligenAuthService {

    String getDiligenJWT(String userName, String password);
}
