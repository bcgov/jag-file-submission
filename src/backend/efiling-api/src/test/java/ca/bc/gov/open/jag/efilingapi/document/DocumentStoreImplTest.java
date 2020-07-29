package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingDocumentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentStoreImpl test suite")
public class DocumentStoreImplTest {

    private static final String DUMMY_CONTENT = "test";
    private static final String DESCRIPTION = "description";
    private DocumentStoreImpl sut;

    @Mock
    private EfilingDocumentService efilingDocumentServiceMock;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        DocumentDetails docummentDetails = new DocumentDetails(DESCRIPTION, BigDecimal.TEN);

        Mockito
                .when(efilingDocumentServiceMock.getDocumentDetails(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(docummentDetails);

        sut = new DocumentStoreImpl(efilingDocumentServiceMock);
    }

    @Test
    @DisplayName("OK: put document should return byte array")
    public void withoutCacheShouldReturnIt() {

        byte[] actual = sut.put("id", DUMMY_CONTENT.getBytes());

        Assertions.assertEquals(DUMMY_CONTENT, new String(actual));
    }

    @Test
    @DisplayName("OK: get document by Id should return null")
    public void withoutCacheShouldReturnNull() {
        Assertions.assertNull(sut.get("id"));
    }

    @Test
    @DisplayName("OK: get document details should cache result")
    public void withCourtLevelCourtClassDocumentTypeShouldReturnDocumentDetails() {


        DocumentDetails actual = sut.getDocumentDetails("courtLevel", "courtClass", "documentType");

        Assertions.assertEquals(DESCRIPTION, actual.getDescription());
        Assertions.assertEquals(BigDecimal.TEN, actual.getStatutoryFeeAmount());

    }


}
