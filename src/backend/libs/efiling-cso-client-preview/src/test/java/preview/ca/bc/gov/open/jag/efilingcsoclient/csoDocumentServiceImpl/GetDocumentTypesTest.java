package preview.ca.bc.gov.open.jag.efilingcsoclient.csoDocumentServiceImpl;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcsoclient.CsoDocumentServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Get Document Type Test Suite")
public class GetDocumentTypesTest {
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

        Mockito.when(filingStatusFacadeBean.getDocumentTypes(Mockito.eq(COURT_LEVEL),any())).thenReturn(Arrays.asList(documentType));
        Mockito.when(filingStatusFacadeBean.getDocumentTypes(Mockito.eq(NODOC),any())).thenReturn(new ArrayList<>());

        Mockito.when(filingStatusFacadeBean.getDocumentTypes(Mockito.eq(EXCEPTION),any())).thenThrow(NestedEjbException_Exception.class);

        sut = new CsoDocumentServiceImpl(filingStatusFacadeBean);
    }

    @DisplayName("OK: test returns document ")
    @Test
    public void testWithFoundResult() {
        List<ca.bc.gov.open.jag.efilingcommons.model.DocumentType> result = sut.getDocumentTypes(COURT_LEVEL, COURT_CLASS);
        Assertions.assertEquals(DESCRIPTION, result.get(0).getDescription());
        Assertions.assertEquals(DOCUMENT_TYPE_CD, result.get(0).getType());
    }

    @DisplayName("Failure: when SOAP service throws NestedEjbException_Exception, service should throw EfilingDocumentServiceException")
    @Test
    public void testThrowException() throws NestedEjbException_Exception {

        Assertions.assertThrows(EfilingDocumentServiceException.class, () -> sut.getDocumentTypes(EXCEPTION, COURT_CLASS));
    }

    @DisplayName("Exception: courtLevel is required")
    @Test
    public void whenCourtLevelIsBlankShouldThrowIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypes(null, "class"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypes("", "class"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypes(" ", "class"));

    }

    @DisplayName("Exception: courtClass is required")
    @Test
    public void whenCourtClassIsBlankShouldThrowIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypes("level", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypes("level", ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getDocumentTypes("level", " "));

    }

}
