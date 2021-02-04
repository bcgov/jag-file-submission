package ca.bc.gov.open.jag.efiling.bdd;

import ca.bc.gov.open.jag.efiling.page.Base;
import io.cucumber.java.After;

public class CucumberHooks extends Base {

    @After("@frontend")
    public void afterScenario() {
        if(this.driver != null)
        this.driver.quit();

    }
}
