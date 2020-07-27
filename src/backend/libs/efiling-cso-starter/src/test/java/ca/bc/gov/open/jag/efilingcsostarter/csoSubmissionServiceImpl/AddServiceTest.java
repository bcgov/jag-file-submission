package ca.bc.gov.open.jag.efilingcsostarter.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.services.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcsostarter.CsoSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapperImpl;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Submission Test")
public class AddServiceTest {
    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    private static final String CLIENT_REFERENCE_TXT = "CLIENTREFERENCETXT";
    private static final String COURT_FILE_NUMBER = "COURTFILENUMBER";
    private static final String DOCUMENTS_PROCESSED = "DOCUMENTPROCESSED";
    private static final Date DATE = DateTime.parse("2018-05-05T11:50:55").toDate();
    private static final String SERVICE_RECEIVED_DTM_TEXT = "SERVICERECIEVEDTXT";
    private static final String SERVICE_SUBTYPE_CD = "SUBTYPE";
    private static final String SERVICE_TYPE_CD = "TYPE_CD";
    private static final String SERVICE_TYPE_DESC = "TYPE_DESC";
    CsoSubmissionServiceImpl sut;

    @Mock
    FilingFacadeBean filingFacadeBeanMock;

    @Mock
    ServiceFacadeBean serviceFacadeBean;

    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);

        sut = new CsoSubmissionServiceImpl(filingFacadeBeanMock, serviceFacadeBean, new ServiceMapperImpl());

    }

    @DisplayName("Exception: with null service should throw IllegalArgumentException")
    @Test
    public void testWithEmptyServiceId() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.addService(null));
    }

    @DisplayName("OK: addService called with any non-empty service")
    @Test
    public void testWithPopulatedSubmissionId() throws NestedEjbException_Exception, DatatypeConfigurationException {
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(createService());
        EfilingService actual = sut.addService(createEfilingService());
        Assertions.assertEquals(BigDecimal.TEN, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getClientId());
        Assertions.assertEquals(CLIENT_REFERENCE_TXT, actual.getClientReferenceTxt());
        Assertions.assertEquals(COURT_FILE_NUMBER, actual.getCourtFileNumber());
        Assertions.assertEquals(DOCUMENTS_PROCESSED, actual.getDocumentsProcessed());
        Assertions.assertEquals(2018, actual.getEntryDateTime().getYear());
        Assertions.assertEquals("10", actual.getEntryUserId());
        Assertions.assertEquals(BigDecimal.TEN, actual.getServiceId());
        Assertions.assertEquals(2018, actual.getServiceReceivedDateTime().getYear());
        Assertions.assertEquals(SERVICE_RECEIVED_DTM_TEXT, actual.getServiceReceivedDtmText());
        Assertions.assertEquals(BigDecimal.TEN, actual.getServiceSessionId());
        Assertions.assertEquals(SERVICE_SUBTYPE_CD, actual.getServiceSubtypeCd());
        Assertions.assertEquals(SERVICE_TYPE_CD, actual.getServiceTypeCd());
        Assertions.assertEquals(SERVICE_TYPE_DESC, actual.getServiceTypeDesc());
    }
    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(serviceFacadeBean.addService(any())).thenThrow(new ca.bc.gov.ag.csows.services.NestedEjbException_Exception());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.addService(createEfilingService()));
    }
    private EfilingService createEfilingService() throws DatatypeConfigurationException {
        return new EfilingService(
                BigDecimal.TEN,
                BigDecimal.TEN,
                CLIENT_REFERENCE_TXT,
                COURT_FILE_NUMBER,
                DOCUMENTS_PROCESSED,
                getXmlDate(),
                "10",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    private Service createService() throws DatatypeConfigurationException {
        Service service = new Service();
        service.setAccountId(BigDecimal.TEN);
        service.setClientId(BigDecimal.TEN);
        service.setClientReferenceTxt(CLIENT_REFERENCE_TXT);
        service.setCourtFileNo(COURT_FILE_NUMBER);
        service.setDocumentsProcessed(DOCUMENTS_PROCESSED);
        service.setEntDtm(getXmlDate());
        service.setEntUserId("10");
        service.setServiceId(BigDecimal.TEN);
        service.setServiceReceivedDtm(getXmlDate());
        service.setServiceReceivedDtmText(SERVICE_RECEIVED_DTM_TEXT);
        service.setServiceSessionId(BigDecimal.TEN);
        service.setServiceSubtypeCd(SERVICE_SUBTYPE_CD);
        service.setServiceTypeCd(SERVICE_TYPE_CD);
        service.setServiceTypeDesc(SERVICE_TYPE_DESC);
        return service;
    }
    private XMLGregorianCalendar getXmlDate() throws DatatypeConfigurationException {
        GregorianCalendar entryDate = new GregorianCalendar();
        entryDate.setTime(DATE);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(entryDate);
    }
}
