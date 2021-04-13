package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.restrictedDocumentApiDelegateImpl;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.RestrictedDocumentType;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetRestrictedDocumentTypeTest {

    private static final String DOCUMENT_TYPE = "TEST_TYPE";
    private static final String DOCUMENT_TYPE_DESCRIPTION = "TEST_TYPE";
    private static final UUID DOCUMENT_ID = UUID.randomUUID();
    private static final UUID EXISTING_DOCUMENT_ID = UUID.randomUUID();


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

        restrictedDocumentType.setId(EXISTING_DOCUMENT_ID);

        Mockito.when(restrictedDocumentRepositoryMock.findById(ArgumentMatchers.eq(EXISTING_DOCUMENT_ID))).thenReturn(Optional.of(restrictedDocumentType));

        Mockito.when(restrictedDocumentRepositoryMock.findById(ArgumentMatchers.eq(DOCUMENT_ID))).thenReturn(Optional.empty());

        sut = new RestrictedDocumentApiDelegateImpl(restrictedDocumentRepositoryMock, new RestrictedDocumentTypeMapperImpl());

    }

    @Test
    @DisplayName("200: get documentType ")
    public void withValidRequest() {

        ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType> actual = sut.getRestrictedDocumentType(EXISTING_DOCUMENT_ID);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(EXISTING_DOCUMENT_ID, actual.getBody().getId());
        Assertions.assertEquals(DOCUMENT_TYPE, actual.getBody().getDocumentType().getType());
        Assertions.assertEquals(DOCUMENT_TYPE_DESCRIPTION, actual.getBody().getDocumentType().getDescription());

    }

    @Test
    @DisplayName("404: get list of documentTypes ")
    public void withMissingDocumentType() {

        ResponseEntity<ca.bc.gov.open.jag.efilingreviewerapi.api.model.RestrictedDocumentType> actual = sut.getRestrictedDocumentType(DOCUMENT_ID);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

}
