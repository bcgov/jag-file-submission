package ca.bc.gov.open.jag.efilingapi.file;

import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingFileException;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.MessageFormat;

import static org.hamcrest.Matchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FileUploadImplTest test suite")
public class FileUploadImplTest {
    private static final String LOCATION = FileSystems.getDefault()
            .getPath("")
            .toAbsolutePath()
            .toString();
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
    public void withByteArrayUploadFile() throws IOException {
        File testFile = new File(MessageFormat.format("{0}/{1}", LOCATION, "test.txt"));

        testFile.deleteOnExit();

        sut.upload(new byte[]{}, "test.txt");

        Assertions.assertTrue(testFile.exists());

        testFile.delete();

    }

    @Test
    @DisplayName("Exception files failed to upload")
    public void updloadThrowsException() {
        Assertions.assertThrows(EfilingFileException.class, () -> sut.upload(null, null));
    }

}
