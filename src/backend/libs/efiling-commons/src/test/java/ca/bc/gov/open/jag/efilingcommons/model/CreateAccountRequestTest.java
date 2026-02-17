package ca.bc.gov.open.jag.efilingcommons.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateAccountRequestTest {

    @Test
    public void shouldBuildACreateAccountRequest() {


        CreateAccountRequest actual = CreateAccountRequest.builder()
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .create();
        
    }

}
