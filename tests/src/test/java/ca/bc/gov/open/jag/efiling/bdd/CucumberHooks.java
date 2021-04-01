package ca.bc.gov.open.jag.efiling.bdd;

import ca.bc.gov.open.jag.efiling.page.BasePage;

public class CucumberHooks extends BasePage {

    //@After("@frontend")
    public void afterScenario() {
        if (this.driver != null)
            this.driver.quit();
    }
}
