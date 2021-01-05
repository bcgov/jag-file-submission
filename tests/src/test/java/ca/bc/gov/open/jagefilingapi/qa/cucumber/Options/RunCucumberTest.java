package ca.bc.gov.open.jagefilingapi.qa.cucumber.Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
         features = {"src/test/java/features"},
         glue ={"stepDefinitions"},
         monochrome = true,
         plugin = {
                 "pretty",
                 "html:target/cucumber-reports/cucumber-pretty",
                 "json:target/cucumber-reports/CucumberTestReport.json",
                 "rerun:target/cucumber-reports/rerun.txt",
                 "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
                }
        )
public class RunCucumberTest {

}
