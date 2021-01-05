package ca.bc.gov.efilinghub.test;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;

@CucumberContextConfiguration
public class MyStepdefs {

    @Value("${test:http://localhost:8081}")
    private String test;


    @When("^test$")
    public void the_client_issues_GET_version() throws Throwable{
        System.out.println("test");

        Assert.assertEquals("http://localhost:8081", test);
    }

}
