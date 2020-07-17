package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentStoreImpl test suite")
public class DocumentStoreImplTest {

    public static final String DUMMY_CONTENT = "test";
    private DocumentStoreImpl sut;

    @Mock
    private EfilingDocumentService efilingDocumentServiceMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new DocumentStoreImpl(efilingDocumentServiceMock);
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
