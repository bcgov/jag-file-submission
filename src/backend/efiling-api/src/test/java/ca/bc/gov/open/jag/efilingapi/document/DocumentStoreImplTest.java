package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentStoreImpl test suite")
public class DocumentStoreImplTest {

    private static final String DUMMY_CONTENT = "test";
    private static final String DESCRIPTION = "description";
    private static final String TYPE = "TYPE";
    private DocumentStoreImpl sut;

    @Mock
    private EfilingDocumentService efilingDocumentServiceMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        DocumentTypeDetails docummentDetails = new DocumentTypeDetails(DESCRIPTION, TYPE, BigDecimal.TEN, true, true, true);

        Mockito
                .when(efilingDocumentServiceMock.getDocumentTypeDetails(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(docummentDetails);


        List<DocumentTypeDetails> documentTypeDetails = Arrays.asList(new DocumentTypeDetails(DESCRIPTION, TYPE, BigDecimal.TEN, true, true, true));

        Mockito
                .when(efilingDocumentServiceMock.getDocumentTypes(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(documentTypeDetails);

        sut = new DocumentStoreImpl(efilingDocumentServiceMock);
    }

    @Test
    @DisplayName("OK: put document should return byte array")
    public void withoutCacheShouldReturnIt() {

        byte[] actual = sut.put(new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()), "filename.txt", DUMMY_CONTENT.getBytes());

        Assertions.assertEquals(DUMMY_CONTENT, new String(actual));
    }

    @Test
    @DisplayName("OK: get document by Id should return null")
    public void withoutCacheShouldReturnNull() {
        Assertions.assertNull(sut.get(new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()), "filename.txt"));
    }

    @Test
    @DisplayName("OK: evict should delete submission")
    public void withoutCacheNotThrowException() {
        Assertions.assertDoesNotThrow(() -> sut.evict(new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()), "filename.txt"));
    }

    @Test
    @DisplayName("OK: put document should return byte array")
    public void withRushCacheShouldReturnIt() {

        byte[] actual = sut.putRushDocument(new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()), "filename.txt", DUMMY_CONTENT.getBytes());

        Assertions.assertEquals(DUMMY_CONTENT, new String(actual));
    }

    @Test
    @DisplayName("OK: get document by Id should return null")
    public void withRushCacheShouldReturnNull() {
        Assertions.assertNull(sut.getRushDocument(new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()), "filename.txt"));
    }

    @Test
    @DisplayName("OK: evict should delete submission")
    public void withRushCacheNotThrowException() {
        Assertions.assertDoesNotThrow(() -> sut.evictRushDocument(new SubmissionKey(UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID()), "filename.txt"));
    }

    @Test
    @DisplayName("OK: get document details should cache result")
    public void withCourtLevelCourtClassDocumentTypeShouldReturnDocumentDetails() {


        DocumentTypeDetails actual = sut.getDocumentDetails("courtLevel", "courtClass", "documentType");

        Assertions.assertEquals(DESCRIPTION, actual.getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getStatutoryFeeAmount());

    }

    @Test
    @DisplayName("OK: get document types should cache result")
    public void withCourtLevelCourtClassShouldReturnDocumentTypes() {


        List<DocumentTypeDetails> actual = sut.getDocumentTypes("courtLevel", "courtClass");

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(DESCRIPTION, actual.get(0).getDescription());
        Assertions.assertEquals(TYPE, actual.get(0).getType());

    }

}
