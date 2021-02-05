package ca.bc.gov.open.jag.efilingcourtdocumentapiclient.config;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CourtDocumentProperties")
public class CourtDocumentPropertiesTest {

    private static final String BASE_PATH = "AVALUE";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Test
    public void testProperties() {
        CourtDocumentProperties courtDocumentProperties = new CourtDocumentProperties();
        courtDocumentProperties.setBasePath(BASE_PATH);
        courtDocumentProperties.setUsername(USERNAME);
        courtDocumentProperties.setPassword(PASSWORD);
        Assertions.assertEquals(BASE_PATH, courtDocumentProperties.getBasePath());
        Assertions.assertEquals(USERNAME, courtDocumentProperties.getUsername());
        Assertions.assertEquals(PASSWORD, courtDocumentProperties.getPassword());
    }

}
