package ca.bc.gov.open.jag.efilingcsostarter.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CsoPropertiesTest {

    private static final String FILE_SERVER_HOST = "fileServerHost";
    private static final String CSO_BASE_PATH = "csobasepath";
    private static final String CSO_PACKAGE_PATH = "csopackagepath";

    @Test
    public void testCsoProperties() {

        CsoProperties sut = new CsoProperties();
        sut.setDebugEnabled(true);
        sut.setFileServerHost(FILE_SERVER_HOST);
        sut.setCsoBasePath(CSO_BASE_PATH);
        sut.setCsoPackagePath(CSO_PACKAGE_PATH);

        Assertions.assertTrue(sut.isDebugEnabled());
        Assertions.assertEquals(FILE_SERVER_HOST, sut.getFileServerHost());
        Assertions.assertEquals(CSO_BASE_PATH, sut.getCsoBasePath());
        Assertions.assertEquals(CSO_PACKAGE_PATH, sut.getCsoPackagePath());
    }
}
