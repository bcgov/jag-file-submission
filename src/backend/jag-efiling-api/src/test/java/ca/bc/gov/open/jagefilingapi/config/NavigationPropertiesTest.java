package ca.bc.gov.open.jagefilingapi.config;

import ca.bc.gov.open.jagefilingapi.fee.models.FeeRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("NavigationProperties Test Suite")
public class NavigationPropertiesTest {

    private static final String BASEURL = "BASEURL";

    @Test
    @DisplayName("CASE1: Test getters and setters")
    public void testGettSetter() {
        NavigationProperties testproperties = new NavigationProperties();
        testproperties.setBaseUrl(BASEURL);
        Assertions.assertEquals(BASEURL, testproperties.getBaseUrl());
    }
}
