package ca.bc.gov.open.jag.efilingsubmissionclient.config;

import ca.bc.gov.ag.csows.filing.FilingFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

@DisplayName("AutoConfiguration Test")
public class AutoConfigurationTest {

    private AutoConfiguration sut;

    @Test
    @DisplayName("CASE2: with malformed url should throw MalformedException")
    public void withMalformedUrlShouldTrhowException() {

        CsoSubmissionProperties properties = new CsoSubmissionProperties();
        properties.setFilingSubmissionSoapUri("test");

        sut = new AutoConfiguration(properties);

        Assertions.assertThrows(MalformedURLException.class, () -> {


            FilingFacade actual = sut.eFilingSubmissionService();

        });

    }
}
