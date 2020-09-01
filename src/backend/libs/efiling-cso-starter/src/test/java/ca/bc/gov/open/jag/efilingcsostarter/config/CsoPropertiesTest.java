package ca.bc.gov.open.jag.efilingcsostarter.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CsoPropertiesTest {

    private static final String FILE_SERVER_HOST = "fileServerHost";
    private static final String CSO_BASE_PATH = "csobasepath";

    @Test
    public void testCsoProperties() {

        CsoProperties sut = new CsoProperties();
        sut.setDebugEnabled(true);
        sut.setFileServerHost(FILE_SERVER_HOST);
        sut.setCsoBasePath(CSO_BASE_PATH);

        Assertions.assertTrue(sut.isDebugEnabled());
        Assertions.assertEquals(FILE_SERVER_HOST, sut.getFileServerHost());
        Assertions.assertEquals(CSO_BASE_PATH, sut.getCsoBasePath());
    }
}
