package ca.bc.gov.open.jag.efilingapi.document.documentApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtClassification;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLevel;
import ca.bc.gov.open.jag.efilingapi.api.model.DocumentTypes;
import ca.bc.gov.open.jag.efilingapi.api.model.EfilingError;
import ca.bc.gov.open.jag.efilingapi.document.DocumentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.ErrorResponse;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("LookupApiDelegateImpl test suite")
public class GetDocumentTypesTest {

    private static final String CLASS_STRING = "S";
    private static final String LEVEL_STRING = "A";
    private static final String CLASS_STRING_ERROR = "M";
    DocumentApiDelegateImpl sut;

    @Mock
    private DocumentStore documentStoreMock;


    @BeforeAll
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        List<DocumentType> documentTypes = Arrays.asList(new DocumentType(TestHelpers.DESCRIPTION, TestHelpers.TYPE.getValue(), true));

        Mockito.when(documentStoreMock.getDocumentTypes(LEVEL_STRING, CLASS_STRING)).thenReturn(documentTypes);
        Mockito.when(documentStoreMock.getDocumentTypes(LEVEL_STRING, CLASS_STRING_ERROR)).thenThrow(new EfilingDocumentServiceException("NOOOOOOO"));
        sut = new DocumentApiDelegateImpl(documentStoreMock);
    }

    @Test
    @DisplayName("200")
    public void withValidParamtersReturnDocumentProperties() {

        ResponseEntity<DocumentTypes> actual = sut.getDocumentTypes(CourtLevel.A, CourtClassification.S);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(1, actual.getBody().getDocumentTypes().size());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().getDocumentTypes().get(0).getDescription());
        Assertions.assertEquals(TestHelpers.TYPE.getValue(), actual.getBody().getDocumentTypes().get(0).getType());
    }

    @Test
    @DisplayName("500")
    public void withExceptionThrownFromSoapInternalServerError() {
        ResponseEntity actual = sut.getDocumentTypes(CourtLevel.A, CourtClassification.M);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorCode(), ((EfilingError)actual.getBody()).getError());
        Assertions.assertEquals(ErrorResponse.DOCUMENT_TYPE_ERROR.getErrorMessage(), ((EfilingError)actual.getBody()).getMessage());
    }

}
