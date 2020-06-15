package ca.bc.gov.open.jag.efilingworker.service;

import org.junit.jupiter.api.*;

import java.io.File;

@DisplayName("MockDocumentStoreService uploadFile Test Suite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MockDocumentStoreServiceImplTest {

    private MockDocumentStoreServiceImpl sut;

    @BeforeAll
    public void setUp() {
        sut = new MockDocumentStoreServiceImpl();
    }

    @Test
    @DisplayName("CASE1: with any request should return a string")
    public void withAnyRequestShouldReturnSeven() {

        String actual = sut.uploadFile(new File(""));

        Assertions.assertNotNull(actual);

    }

}
