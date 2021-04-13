package ca.bc.gov.open.jag.efilingreviewerapi.restricteddocument.restrictedDocumentApiDelegateImpl;

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
public class DeleteRestrictedDocumentTypeTest {

    private static final UUID DOCUMENT_ID = UUID.randomUUID();
    private static final UUID EXISTING_DOCUMENT_ID = UUID.randomUUID();

    private RestrictedDocumentApiDelegateImpl sut;

    @Mock
    RestrictedDocumentRepository restrictedDocumentRepositoryMock;


    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(restrictedDocumentRepositoryMock.existsById(ArgumentMatchers.eq(EXISTING_DOCUMENT_ID))).thenReturn(false);

        Mockito.when(restrictedDocumentRepositoryMock.existsById(ArgumentMatchers.eq(DOCUMENT_ID))).thenReturn(true);

        Mockito.doNothing().when(restrictedDocumentRepositoryMock).deleteById(any());


        sut = new RestrictedDocumentApiDelegateImpl(restrictedDocumentRepositoryMock, new RestrictedDocumentTypeMapperImpl());

    }

    @Test
    @DisplayName("204: document deleted ")
    public void withValidRequest() {

        ResponseEntity actual = sut.deleteRestrictedDocumentType(EXISTING_DOCUMENT_ID);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    @Test
    @DisplayName("404: document not found ")
    public void withNoDocumentExceptionThrown() {

        ResponseEntity actual = sut.deleteRestrictedDocumentType(DOCUMENT_ID);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }


}
