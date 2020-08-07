package ca.bc.gov.open.jag.efilingapi.file;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FileUploadConfigTest test suite")
public class FileUploadConfigTest {
    private FileUploadConfig sut;
    @Test
    @DisplayName("get FileUploadServiceImpl test")
    public void testGetSubmissionService() {
        sut = new FileUploadConfig(null);
        FileUploadService actual = sut.fileUploadService();
        Assertions.assertEquals(FileUploadServiceImpl.class, actual.getClass());

    }

}
