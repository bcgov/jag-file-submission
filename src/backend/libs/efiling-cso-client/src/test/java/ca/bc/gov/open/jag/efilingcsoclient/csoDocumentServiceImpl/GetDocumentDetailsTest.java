package ca.bc.gov.open.jag.efilingcsoclient.csoDocumentServiceImpl;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcsoclient.CsoDocumentServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Get Document Details Test Suite")
public class GetDocumentDetailsTest {

    private static final String DOCUMENT_TYPE_CD = "ACODE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String NODOC = "NODOC";
    public static final String COURT_LEVEL = "level1";
    public static final String COURT_CLASS = "class1";
    public static final String EXCEPTION = "exception";
    @Mock
    FilingStatusFacadeBean filingStatusFacadeBean;

    private static CsoDocumentServiceImpl sut;

    @BeforeAll
    public void setUp() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);
        DocumentType documentType = new DocumentType();
        documentType.setDocumentTypeCd(DOCUMENT_TYPE_CD);
        documentType.setDocumentTypeDesc(DESCRIPTION);
        documentType.setDefaultStatutoryFee(BigDecimal.TEN);
        documentType.setOrderDocumentYn(true);
        documentType.setRushRequiredYn(true);
        documentType.setAutoProcessYn(false);


        Mockito.when(filingStatusFacadeBean.getDocumentTypes(any(), Mockito.eq(COURT_LEVEL),any())).thenReturn(Arrays.asList(documentType));
        Mockito.when(filingStatusFacadeBean.getDocumentTypes(any(), Mockito.eq(NODOC),any())).thenReturn(new ArrayList<>());

        Mockito.when(filingStatusFacadeBean.getDocumentTypes(any(), Mockito.eq(EXCEPTION),any())).thenThrow(NestedEjbException_Exception.class);

        sut = new CsoDocumentServiceImpl(filingStatusFacadeBean);
    }

    @DisplayName("OK: test returns document ")
    @Test
    public void testWithFoundResult() {
        DocumentTypeDetails result = sut.getDocumentTypeDetails(COURT_LEVEL, COURT_CLASS, DOCUMENT_TYPE_CD, "");
        Assertions.assertEquals(DESCRIPTION, result.getDescription());
        Assertions.assertEquals(BigDecimal.TEN, result.getStatutoryFeeAmount());
        Assertions.assertTrue(result.isOrderDocument());
        Assertions.assertTrue(result.isRushRequired());
        Assertions.assertFalse(result.isAutoProcessing());
    }

    @DisplayName("Exception: when not finding document should throw exception")
    @Test
    public void whenNotFindingDocumentsShouldThrowError() {

        Assertions.assertThrows(EfilingDocumentServiceException.class, () -> sut.getDocumentTypeDetails(NODOC, COURT_CLASS, NODOC, ""));
    }

    @DisplayName("Failure: when SOAP service throws NestedEjbException_Exception, service should throw EfilingDocumentServiceException")
    @Test
    public void testThrowException() throws NestedEjbException_Exception {

        Assertions.assertThrows(EfilingDocumentServiceException.class, () -> sut.getDocumentTypeDetails(EXCEPTION, COURT_CLASS,"type", ""));
    }

    @DisplayName("Exception: courtLevel is required")
    @Test
    public void whenCourtLevelIsBlankShouldThrowIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails(null, "class", "type", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("", "class", "type", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails(" ", "class", "type", ""));

    }

    @DisplayName("Exception: courtClass is required")
    @Test
    public void whenCourtClassIsBlankShouldThrowIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("level", null, "type", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("level", "", "type", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("level", " ", "type", ""));

    }


    @DisplayName("Exception: documentType is required")
    @Test
    public void whenDocumentTypeIsBlankShouldThrowIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("level", "class", null, ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("level", "class", "", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypeDetails("level", "class", " ", ""));

    }

}
