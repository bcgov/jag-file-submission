package ca.bc.gov.open.jag.efilingapi.document.documentApiDelegateImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtClassification;
import ca.bc.gov.open.jag.efilingapi.api.model.CourtLevel;
import ca.bc.gov.open.jag.efilingapi.document.DocumentApiDelegateImpl;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.error.DocumentTypeException;
import ca.bc.gov.open.jag.efilingapi.error.ErrorCode;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
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
    public void beforeAll() throws IOException {
        MockitoAnnotations.openMocks(this);
        List<DocumentTypeDetails> documentTypeDetails = Arrays.asList(new DocumentTypeDetails(TestHelpers.DESCRIPTION, TestHelpers.TYPE, BigDecimal.TEN, false, false, false));

        Mockito.when(documentStoreMock.getDocumentTypes(LEVEL_STRING, CLASS_STRING)).thenReturn(documentTypeDetails);
        Mockito.when(documentStoreMock.getDocumentTypes(LEVEL_STRING, CLASS_STRING_ERROR)).thenThrow(new EfilingDocumentServiceException("NOOOOOOO"));
        sut = new DocumentApiDelegateImpl(documentStoreMock);
    }

    @Test
    @DisplayName("200")
    public void withValidParametersReturnDocumentProperties() {

        ResponseEntity<List<ca.bc.gov.open.jag.efilingapi.api.model.DocumentType>> actual = sut.getDocumentTypes(CourtLevel.A, CourtClassification.S);

        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(1, actual.getBody().size());
        Assertions.assertEquals(TestHelpers.DESCRIPTION, actual.getBody().get(0).getDescription());
        Assertions.assertEquals(TestHelpers.TYPE, actual.getBody().get(0).getType());

    }

    @Test
    @DisplayName("500: exception thrown from SOAP should throw DocumentTypeException")
    public void withExceptionThrownFromSoapShouldThrowDocumentTypeException() {

        DocumentTypeException exception = Assertions.assertThrows(DocumentTypeException.class, () -> sut.getDocumentTypes(CourtLevel.A, CourtClassification.M));
        Assertions.assertEquals(ErrorCode.DOCUMENT_TYPE_ERROR.toString(), exception.getErrorCode());
    }
}
