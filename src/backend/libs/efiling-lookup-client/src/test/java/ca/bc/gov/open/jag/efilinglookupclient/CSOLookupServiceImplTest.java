package ca.bc.gov.open.jag.efilinglookupclient;

import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CSO Efiling Lookup Client Test Suite")
public class CSOLookupServiceImplTest {

    private static final String SERVICE_ID = "ServiceId";
    public static final String ENT_USER_ID = "entUserId";
    public static final String SERVICE_TYPE_CD = "serviceTypeCd";
    public static final String UPD_USER_ID = "updUserId";
    public static final String SERVICE_ID_EXCEPTION = "exception";

    CSOLookupServiceImpl sut;

    @Mock
    LookupFacadeBean lookupFacadeBeanMock;

    @Mock
    ServiceFee serviceFeeMock;
    @Mock
    private XMLGregorianCalendar updDtmMock;
    @Mock
    private XMLGregorianCalendar expiryDtMock;
    @Mock
    private XMLGregorianCalendar entDtmMock;
    @Mock
    private XMLGregorianCalendar effectiveDtMock;


    @BeforeEach
    public void init() throws NestedEjbException_Exception, DatatypeConfigurationException {

        MockitoAnnotations.initMocks(this);
        Mockito.when(serviceFeeMock.getFeeAmt()).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFeeMock.getEntUserId()).thenReturn(ENT_USER_ID);
        Mockito.when(serviceFeeMock.getServiceTypeCd()).thenReturn(SERVICE_TYPE_CD);
        Mockito.when(serviceFeeMock.getUpdUserId()).thenReturn(UPD_USER_ID);
        updDtmMock = DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-07-01T13:54:46.773-07:00");
        expiryDtMock= DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-07-02T13:54:46.773-07:00");
        entDtmMock= DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-07-03T13:54:46.773-07:00");
        effectiveDtMock= DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-07-15T13:54:46.773-07:00");
        Mockito
                .when(serviceFeeMock.getEffectiveDt())
                .thenReturn(effectiveDtMock);
        Mockito
                .when(serviceFeeMock.getEntDtm())
                .thenReturn(entDtmMock);
        Mockito
                .when(serviceFeeMock.getExpiryDt())
                .thenReturn(expiryDtMock);
        Mockito
                .when(serviceFeeMock.getUpdDtm())
                .thenReturn(updDtmMock);

        Mockito
                .when(lookupFacadeBeanMock.getServiceFee(Mockito.eq(SERVICE_ID), any()))
                .thenReturn(serviceFeeMock);

        Mockito
                .when(lookupFacadeBeanMock.getServiceFee(Mockito.eq(SERVICE_ID_EXCEPTION), Mockito.any()))
                .thenThrow(new NestedEjbException_Exception("random"));

        sut = new CSOLookupServiceImpl(lookupFacadeBeanMock);

    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyServiceId() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(""));
    }

    @DisplayName("OK: getServiceFee called with any non-empty serviceId")
    @Test
    public void testWithPopulatedServiceId() {

        ServiceFees actual = sut.getServiceFee(SERVICE_ID);
        Assertions.assertEquals(BigDecimal.TEN, actual.getFeeAmt());
        Assertions.assertEquals(15, DateTime.parse(actual.getEffectiveDt()).getDayOfMonth());
        Assertions.assertEquals(3, DateTime.parse(actual.getEntDtm()).getDayOfMonth());
        Assertions.assertEquals(ENT_USER_ID, actual.getEntUserId());
        Assertions.assertEquals(2, DateTime.parse(actual.getExpiryDt()).getDayOfMonth());
        Assertions.assertEquals(SERVICE_TYPE_CD, actual.getServiceTypeCd());
        Assertions.assertEquals(UPD_USER_ID, actual.getUpdUserId());
        Assertions.assertEquals(1, DateTime.parse(actual.getUdpDtm()).getDayOfMonth());

    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingLookupServiceException() {

        Assertions.assertThrows(EfilingLookupServiceException.class, () -> sut.getServiceFee(SERVICE_ID_EXCEPTION));

    }

}
