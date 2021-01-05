package ca.bc.gov.efilinghub.test;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources")
@SpringBootTest(classes = TestConfig.class)
public class CucumberIntegrationTest {
}
