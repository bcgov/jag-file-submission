package ca.bc.gov.open.jag.efilingapi.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FileUploadImplTest test suite")
public class FileUploadImplTest {
    FileUploadServiceImpl sut;

    @Test
    @DisplayName("OK: files are uploaded")
    public void withByteArrayUploadFile() {

    }

    @Test
    @DisplayName("Exception files failed")
    public void updloadThrowsException() {

    }

}
