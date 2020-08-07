package ca.bc.gov.open.jag.efilingapi.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FileUploadPropertiesTest test suite")
public class FileUploadPropertiesTest {

    private static final String LOCATION = "IMMALOCATION";

    FileUploadProperties sut;

    @Test
    @DisplayName("get FileUploadProperties test")
    public void testGetSubmissionService() {

        sut = new FileUploadProperties();
        sut.setLocation(LOCATION);

        Assertions.assertEquals(LOCATION, sut.getLocation());

    }
}
