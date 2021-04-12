package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.restrictedDocumentApiDelegateImpl;

import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.RestrictedDocumentType;
import ca.bc.gov.open.jag.efilingreviewerapi.document.store.RestrictedDocumentRepository;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.RestrictedDocumentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.mappers.RestrictedDocumentTypeMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateRestrictedDocumentTypeTest {

    private static final String DOCUMENT_TYPE = "TEST_TYPE";
    private static final String DOCUMENT_TYPE_DESCRIPTION = "TEST_TYPE";
    private static final UUID TEST_UUID = UUID.randomUUID();


    private RestrictedDocumentApiDelegateImpl sut;

    @Mock
    RestrictedDocumentRepository restrictedDocumentRepositoryMock;


    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        RestrictedDocumentType restrictedDocumentType = createRestrictedDocumentType();
        restrictedDocumentType.setId(TEST_UUID);

        Mockito.when(restrictedDocumentRepositoryMock.save(any())).thenReturn(restrictedDocumentType);

        sut = new RestrictedDocumentApiDelegateImpl(restrictedDocumentRepositoryMock, new RestrictedDocumentTypeMapperImpl());

    }

    @Test
    @DisplayName("200: document type creates ")
    public void withValidRequest() {

        DocumentType documentType = new DocumentType();
        documentType.setType(DOCUMENT_TYPE);
        documentType.setDescription(DOCUMENT_TYPE_DESCRIPTION);

        ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType> actual = sut.createRestrictedDocumentType(documentType);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(TEST_UUID, actual.getBody().getId());
        Assertions.assertEquals(DOCUMENT_TYPE, actual.getBody().getDocumentType().getType());
        Assertions.assertEquals(DOCUMENT_TYPE_DESCRIPTION, actual.getBody().getDocumentType().getDescription());


    }

    @Test
    @DisplayName("400: exception thrown ")
    public void withInValidRequest() {

        DocumentType documentType = new DocumentType();
        documentType.setType("");

        ResponseEntity actual = sut.createRestrictedDocumentType(documentType);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());


    }

    private RestrictedDocumentType createRestrictedDocumentType() {
        return RestrictedDocumentType.builder()
                .documentType(DOCUMENT_TYPE)
                .documentTypeDescription(DOCUMENT_TYPE_DESCRIPTION)
                .create();
    }

}
