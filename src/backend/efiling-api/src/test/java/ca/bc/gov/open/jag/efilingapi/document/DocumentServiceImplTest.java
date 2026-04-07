package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DocumentServiceImplTest {

    private static final String COURT_LEVEL = "courtLevel";
    private static final String COURT_CLASSIFICATION = "court classification";

    private DocumentServiceImpl sut;

    @Mock
    private EfilingDocumentService efilingDocumentServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        List<DocumentTypeDetails> documentList = new ArrayList<>();
        DocumentTypeDetails document = new DocumentTypeDetails("description", "type", BigDecimal.TEN,false, true, true);
        documentList.add(document);
        Mockito.when(efilingDocumentServiceMock.getDocumentTypes(Mockito.eq(COURT_LEVEL), Mockito.eq(COURT_CLASSIFICATION), Mockito.any())).thenReturn(documentList);

        sut = new DocumentServiceImpl(efilingDocumentServiceMock);


    }

    @Test
    @DisplayName("ok: should return a list of documents")
    public void shouldReturnAListOfDocuments() {

        List<DocumentTypeDetails> actual = sut.getValidDocumentTypes(GetValidDocumentTypesRequest
                .builder()
                .courtLevel(COURT_LEVEL)
                .courtClassification(COURT_CLASSIFICATION)
                .create());

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("description", actual.get(0).getDescription());
        Assertions.assertEquals("type", actual.get(0).getType());


    }


}
