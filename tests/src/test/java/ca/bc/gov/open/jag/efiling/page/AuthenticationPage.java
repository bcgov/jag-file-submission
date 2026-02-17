package ca.bc.gov.open.jag.efiling.page;

import java.io.FileNotFoundException;

public interface AuthenticationPage {

    String getName();

    void signIn() throws FileNotFoundException;
}
