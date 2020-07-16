package ca.bc.gov.open.jag.efilingapi.document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentConfiguration")
public class DocumentConfigurationTest {

    private DocumentConfiguration sut;

    @BeforeAll
    public void setup() {
        sut = new DocumentConfiguration();
    }


    @DisplayName("OK: check types")
    public void checkRegisteredBeans() {

        DocumentStore actual = sut.documentStore();

        Assertions.assertEquals(DocumentStoreImpl.class, actual.getClass());

    }



}
