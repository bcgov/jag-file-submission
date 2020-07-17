package ca.bc.gov.open.jag.efilingaccountclient.csoStatusServiceImpl;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingaccountclient.CSODocumentServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
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

    private static CSODocumentServiceImpl sut;

    @BeforeAll
    public void setUp() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);
        DocumentType documentType = new DocumentType();
        documentType.setDocumentTypeCd(DOCUMENT_TYPE_CD);
        documentType.setDocumentTypeDesc(DESCRIPTION);
        documentType.setDefaultStatutoryFee(BigDecimal.TEN);

        Mockito.when(filingStatusFacadeBean.getDocumentTypes(Mockito.eq(COURT_LEVEL),any())).thenReturn(Arrays.asList(documentType));
        Mockito.when(filingStatusFacadeBean.getDocumentTypes(Mockito.eq(NODOC),any())).thenReturn(new ArrayList<>());

        Mockito.when(filingStatusFacadeBean.getDocumentTypes(Mockito.eq(EXCEPTION),any())).thenThrow(NestedEjbException_Exception.class);

        sut = new CSODocumentServiceImpl(filingStatusFacadeBean);
    }

    @DisplayName("OK: test returns document ")
    @Test
    public void testWithFoundResult() {
        DocumentDetails result = sut.getDocumentDetails(COURT_LEVEL, COURT_CLASS, DOCUMENT_TYPE_CD);
        Assertions.assertEquals(DESCRIPTION, result.getDescription());
        Assertions.assertEquals(BigDecimal.TEN, result.getStatutoryFeeAmount());
    }

    @DisplayName("OK: test returns null ")
    @Test
    public void testWithNoResult() {
        DocumentDetails result = sut.getDocumentDetails(NODOC, COURT_CLASS, NODOC);
        Assertions.assertNull(result);
    }

    @DisplayName("Failure: throws exception")
    @Test
    public void testThrowException() throws NestedEjbException_Exception {

        Assertions.assertThrows(EfilingLookupServiceException.class, () -> sut.getDocumentDetails(EXCEPTION, COURT_CLASS,""));
    }
}
