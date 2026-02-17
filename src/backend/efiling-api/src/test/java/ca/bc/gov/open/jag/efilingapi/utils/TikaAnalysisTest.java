package ca.bc.gov.open.jag.efilingapi.utils;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("TikaAnalysis Test Suite")
public class TikaAnalysisTest {

    @Mock
    MultipartFile multipartFileMock;


    @BeforeEach
    public void setup() throws IOException {

        MockitoAnnotations.openMocks(this);

        Mockito.doThrow(IOException.class).when(multipartFileMock).getBytes();
    }

    @Test
    @DisplayName("Test success")
    public void withValidFileReturnTrue() throws IOException {
        File file = new File("src/test/resources/test.pdf");

        MultipartFile multipartFile = new MockMultipartFile("test.pdf", new FileInputStream(file));

        Assertions.assertTrue(TikaAnalysis.isPdf(multipartFile));
    }

    @Test
    @DisplayName("Test failure")
    public void withInValidFileReturnFalse() {
        Assertions.assertFalse(TikaAnalysis.isPdf(multipartFileMock));
    }
}
