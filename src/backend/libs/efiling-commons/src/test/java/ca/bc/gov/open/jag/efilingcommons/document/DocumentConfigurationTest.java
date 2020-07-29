package ca.bc.gov.open.jag.efilingcommons.document;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentConfiguration")
public class DocumentConfigurationTest {

    private DocumentConfiguration sut;

    @BeforeAll
    public void setup() {
        sut = new DocumentConfiguration();
    }



    @Test
    @DisplayName("OK: check types")
    public void checkRegisteredBeans() {

        DocumentStore actual = sut.documentStore(null);

        Assertions.assertEquals(DocumentStoreImpl.class, actual.getClass());

    }



}
