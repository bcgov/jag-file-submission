package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.restrictedDocumentApiDelegateImpl;

import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.RestrictedDocumentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.mappers.RestrictedDocumentTypeMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateRestrictedDocumentTypeTest {

    private static final String DOCUMENT_TYPE = "TEST_TYPE";
    private static final String DOCUMENT_TYPE_DESCRIPTION = "TEST_TYPE";
    private static final UUID EXISTING_ID = UUID.randomUUID();
    private static final UUID NEW_ID = UUID.randomUUID();

    private RestrictedDocumentApiDelegateImpl sut;

    @Mock
    RestrictedDocumentRepository restrictedDocumentRepositoryMock;


    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        ca.bc.gov.open.jag.efilingreviewerapi.document.models.RestrictedDocumentType restrictedDocumentType = ca.bc.gov.open.jag.efilingreviewerapi.document.models.RestrictedDocumentType.builder()
                .documentType(DOCUMENT_TYPE)
                .documentTypeDescription(DOCUMENT_TYPE_DESCRIPTION)
                .create();

        restrictedDocumentType.setId(EXISTING_ID);

        Mockito.when(restrictedDocumentRepositoryMock.save(any())).thenReturn(restrictedDocumentType);

        Mockito.when(restrictedDocumentRepositoryMock.existsById(ArgumentMatchers.eq(EXISTING_ID))).thenReturn(true);

        Mockito.when(restrictedDocumentRepositoryMock.existsById(ArgumentMatchers.eq(NEW_ID))).thenReturn(false);

        sut = new RestrictedDocumentApiDelegateImpl(restrictedDocumentRepositoryMock, new RestrictedDocumentTypeMapperImpl());

    }

    @Test
    @DisplayName("200: get list of documentTypes ")
    public void withValidRequest() {

        RestrictedDocumentType restrictedDocumentType = new RestrictedDocumentType();
        DocumentType documentType = new DocumentType();
        documentType.setType(DOCUMENT_TYPE);
        documentType.setDescription(DOCUMENT_TYPE_DESCRIPTION);
        restrictedDocumentType.setId(EXISTING_ID);
        restrictedDocumentType.setDocumentType(documentType);

        ResponseEntity actual = sut.updateRestrictedDocumentType(restrictedDocumentType);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    @Test
    @DisplayName("400: get list of documentTypes ")
    public void withInValidRequest() {

        RestrictedDocumentType restrictedDocumentType = new RestrictedDocumentType();
        DocumentType documentType = new DocumentType();
        documentType.setType(DOCUMENT_TYPE);
        documentType.setDescription(DOCUMENT_TYPE_DESCRIPTION);
        restrictedDocumentType.setId(null);
        restrictedDocumentType.setDocumentType(documentType);

        ResponseEntity actual = sut.updateRestrictedDocumentType(restrictedDocumentType);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: get list of documentTypes ")
    public void withNoExistingValue() {

        RestrictedDocumentType restrictedDocumentType = new RestrictedDocumentType();
        DocumentType documentType = new DocumentType();
        documentType.setType(DOCUMENT_TYPE);
        documentType.setDescription(DOCUMENT_TYPE_DESCRIPTION);
        restrictedDocumentType.setId(NEW_ID);
        restrictedDocumentType.setDocumentType(documentType);

        ResponseEntity actual = sut.updateRestrictedDocumentType(restrictedDocumentType);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }


}
