package ca.bc.gov.open.jag.efiling;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@RunWith(Cucumber.class)
@CucumberOptions(
         features = {"src/test/resources"},
         glue ={"ca.bc.gov.open.jag.efiling"},
         monochrome = true,
//         tags = "@current", // uncomment to run specific tests
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
