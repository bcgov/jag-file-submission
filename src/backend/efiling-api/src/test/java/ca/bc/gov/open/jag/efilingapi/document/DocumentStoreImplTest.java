package ca.bc.gov.open.jag.efilingapi.document;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentStoreImpl test suite")
public class DocumentStoreImplTest {

    public static final String DUMMY_CONTENT = "test";
    private DocumentStoreImpl sut;

    @BeforeAll
    public void setUp() {
        sut = new DocumentStoreImpl();
    }

    @Test
    @DisplayName("OK: should return byte array")
    public void withoutCacheShouldReturnIt() {

        byte[] actual = sut.put("id", DUMMY_CONTENT.getBytes());

        Assertions.assertEquals(DUMMY_CONTENT, new String(actual));
    }

    @Test
    @DisplayName("OK: should return null")
    public void withoutCacheShouldReturnNull() {

        Assertions.assertNull(sut.get("id"));

    }


}
