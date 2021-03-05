package ca.bc.gov.open.jag.efiling.page;

import java.io.FileNotFoundException;

public interface AuthenticationPage {

    void goTo(String url);
    String getName();
    void signIn() throws FileNotFoundException;
}
