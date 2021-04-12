package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.restrictedDocumentApiDelegateImpl;

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


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateRestrictedDocumentTypeTest {

    private static final String DOCUMENT_TYPE = "TEST_TYPE";
    private static final String EXISTING_TYPE = "EXISTING_TYPE";
    private static final String DOCUMENT_TYPE_DESCRIPTION = "TEST_TYPE";
    private static final UUID TEST_UUID = UUID.randomUUID();

    private RestrictedDocumentApiDelegateImpl sut;

    @Mock
    RestrictedDocumentRepository restrictedDocumentRepositoryMock;


    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        RestrictedDocumentType restrictedDocumentType = RestrictedDocumentType.builder()
                .documentType(DOCUMENT_TYPE)
                .documentTypeDescription(DOCUMENT_TYPE_DESCRIPTION)
                .create();

        restrictedDocumentType.setId(TEST_UUID);

        Mockito.when(restrictedDocumentRepositoryMock.findAll()).thenReturn(Collections.singletonList(restrictedDocumentType));

        sut = new RestrictedDocumentApiDelegateImpl(restrictedDocumentRepositoryMock, new RestrictedDocumentTypeMapperImpl());

    }

    @Test
    @DisplayName("200: get list of documentTypes ")
    public void withValidRequest() {

        ResponseEntity<List<ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType>> actual = sut.getRestrictedDocumentTypes();

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(1, actual.getBody().size());


    }

}
