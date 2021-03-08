package ca.bc.gov.open.jag.efilingreviewerapi.extract.store;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.mocks.ExtractRequestMockFactory;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheExtractStoreTest {


    private CacheExtractStore sut;

    @BeforeAll
    public void beforeAll(){
        sut = new CacheExtractStore();
    }

    @DisplayName("ok: should cache an extract")
    @Test
    public void shouldCacheAnExtract() {

        ExtractRequest expectedExtractRequest = ExtractRequestMockFactory.mock();

        ExtractRequest actual = sut.put(BigDecimal.ONE, expectedExtractRequest).get();

        Assertions.assertEquals(actual.getExtract().getId(), expectedExtractRequest.getExtract().getId());
        Assertions.assertEquals(actual.getExtract().getTransactionId(), expectedExtractRequest.getExtract().getTransactionId());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_CONTENT_TYPE, expectedExtractRequest.getDocument().getContentType());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_FILE_NAME, expectedExtractRequest.getDocument().getFileName());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_SIZE, expectedExtractRequest.getDocument().getSize());
        Assertions.assertEquals(ExtractRequestMockFactory.EXPECTED_DOCUMENT_TYPE, expectedExtractRequest.getDocument().getType());

    }

    @DisplayName("ok: should retrieve null when extract is not stored")
    @Test
    public void shouldRetrieveAnExtract() {

        Optional<ExtractRequest> extractRequest = sut.get(BigDecimal.ONE);
        Assertions.assertFalse(extractRequest.isPresent());

    }

}
