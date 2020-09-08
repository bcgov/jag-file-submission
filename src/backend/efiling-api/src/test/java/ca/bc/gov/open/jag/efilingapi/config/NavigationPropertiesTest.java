package ca.bc.gov.open.jag.efilingapi.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("NavigationProperties Test Suite")
public class NavigationPropertiesTest {

    private static final String BASEURL = "BASEURL";
    private static final String CSO_BASEURL = "CSO_BASEURL";

    @Test
    @DisplayName("CASE1: Test getters and setters")
    public void testGettSetter() {
        NavigationProperties testproperties = new NavigationProperties();
        testproperties.setBaseUrl(BASEURL);
        testproperties.setCsoBaseUrl(CSO_BASEURL);
        Assertions.assertEquals(BASEURL, testproperties.getBaseUrl());
        Assertions.assertEquals(CSO_BASEURL, testproperties.getCsoBaseUrl());
    }
}
