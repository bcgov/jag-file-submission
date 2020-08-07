package ca.bc.gov.open.jag.efilingapi.file;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingFileException;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.Matchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FileUploadImplTest test suite")
public class FileUploadImplTest {
    private static final String LOCATION = "LOCATION";
    FileUploadServiceImpl sut;

    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        FileUploadProperties fileUploadProperties = new FileUploadProperties();
        fileUploadProperties.setLocation(LOCATION);

        sut = new FileUploadServiceImpl(fileUploadProperties);
    }

    @Test
    @DisplayName("OK: files are uploaded")
    public void withByteArrayUploadFile() {


       // PowerMockito.doNothing().when(Files.copy()).copy();
    }

    @Test
    @DisplayName("Exception files failed")
    public void updloadThrowsException() {
        Assertions.assertThrows(EfilingFileException.class, () -> sut.upload(null, null));
    }

}
