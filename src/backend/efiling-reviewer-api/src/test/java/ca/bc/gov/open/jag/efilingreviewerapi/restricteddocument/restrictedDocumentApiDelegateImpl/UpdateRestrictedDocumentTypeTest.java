package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.restrictedDocumentApiDelegateImpl;

import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.RestrictedDocumentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.mappers.RestrictedDocumentTypeMapperImpl;
import org.junit.jupiter.api.*;
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
    private static final UUID TEST_UUID = UUID.randomUUID();

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

        restrictedDocumentType.setId(TEST_UUID);

        Mockito.when(restrictedDocumentRepositoryMock.save(any())).thenReturn(restrictedDocumentType);

        sut = new RestrictedDocumentApiDelegateImpl(restrictedDocumentRepositoryMock, new RestrictedDocumentTypeMapperImpl());

    }

    @Test
    @DisplayName("200: get list of documentTypes ")
    public void withValidRequest() {

        RestrictedDocumentType restrictedDocumentType = new RestrictedDocumentType();
        DocumentType documentType = new DocumentType();
        documentType.setType(DOCUMENT_TYPE);
        documentType.setDescription(DOCUMENT_TYPE_DESCRIPTION);
        restrictedDocumentType.setId(TEST_UUID);
        restrictedDocumentType.setDocumentType(documentType);

        ResponseEntity actual = sut.updateRestrictedDocumentType(restrictedDocumentType);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

}
