package ca.bc.gov.open.jag.efilingcsostarter.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsostarter.CsoSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcsostarter.TestHelpers;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.FinancialTransactionMapperImpl;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Submission Test")
public class SubmitFilingPackageTest {

    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    CsoSubmissionServiceImpl sut;

    @Mock
    FilingFacadeBean filingFacadeBeanMock;

    @Mock
    ServiceFacadeBean serviceFacadeBean;

    @Mock
    EfilingPaymentService efilingPaymentServiceMock;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        UserSession userSession = new UserSession();
        userSession.setStartDtm(DateUtils.getCurrentXmlDate());
        userSession.setUpdUserId("1234");
        userSession.setUserSessionId(new BigDecimal(1234));

        Mockito.when(serviceFacadeBean.createUserSession(Mockito.anyString())).thenReturn(userSession);
        ServiceSession serviceSession = new ServiceSession();
        serviceSession.setUserSessionId(new BigDecimal(1234));
        serviceSession.setServiceSessionId(new BigDecimal(5678));
        Mockito.doReturn(serviceSession).when(serviceFacadeBean)
                .createServiceSession(ArgumentMatchers.argThat(x -> x.getUserSessionId().equals(userSession.getUserSessionId())), Mockito.anyString());

        sut = new CsoSubmissionServiceImpl(filingFacadeBeanMock, serviceFacadeBean, new ServiceMapperImpl(), new FilingPackageMapperImpl(), new FinancialTransactionMapperImpl());

    }

    @DisplayName("Exception: with null service should throw IllegalArgumentException")
    @Test
    public void testWithEmptyService() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(null, new EfilingFilingPackage(), null));
    }

    @DisplayName("Exception: with null filingpackage should throw IllegalArgumentException")
    @Test
    public void testWithEmptyFilingPackage() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(new EfilingService(), null, null));
    }


    @DisplayName("Exception: with null clientId should throw IllegalArgumentException")
    @Test
    public void testWithEmptyClientId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(new EfilingService(), new EfilingFilingPackage(), null));
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionId() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());
        BigDecimal actual = sut.submitFilingPackage(TestHelpers.createBaseEfilingService(), new EfilingFilingPackage(), efilingPaymentServiceMock);
        Assertions.assertEquals(BigDecimal.TEN, actual);
    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenFilingFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws NestedEjbException_Exception, DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception {
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenThrow(new ca.bc.gov.ag.csows.filing.NestedEjbException_Exception());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        EfilingService efilingService = new EfilingService();
        efilingService.setClientId(BigDecimal.TEN);

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(efilingService, new EfilingFilingPackage(), efilingPaymentServiceMock));

    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenServiceFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws NestedEjbException_Exception {
        Mockito.when(serviceFacadeBean.addService(any())).thenThrow(new NestedEjbException_Exception());
        EfilingService efilingService = new EfilingService();
        efilingService.setClientId(BigDecimal.TEN);
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(efilingService, new EfilingFilingPackage(), null));
    }
    
    
    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenCreateServiceSessionThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {
        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).createServiceSession(Mockito.any(), Mockito.anyString());
        EfilingService efilingService = new EfilingService();
        efilingService.setClientId(BigDecimal.TEN);
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(efilingService, new EfilingFilingPackage(), null));
    }

    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenCreateUserSessionThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {
        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).createUserSession(Mockito.anyString());
        EfilingService efilingService = new EfilingService();
        efilingService.setClientId(BigDecimal.TEN);
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(efilingService, new EfilingFilingPackage(), null));
    }


    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenUpdatePaymentForServiceThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {

        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).updateService(ArgumentMatchers.argThat(service -> service.getFeePaidYn().equals(String.valueOf(true))));
        Service service = new Service();
        Mockito.when(serviceFacadeBean.addService(Mockito.any())).thenReturn(service);
        EfilingService efilingService = new EfilingService();
        efilingService.setClientId(BigDecimal.TEN);
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(efilingService, new EfilingFilingPackage(), efilingPaymentServiceMock));
      
    }
    private EfilingTransaction createTransaction() {
        EfilingTransaction efilingTransaction = new EfilingTransaction();
        efilingTransaction.setApprovalCd("Approved");
        efilingTransaction.setEcommerceTransactionId(BigDecimal.TEN);
        return efilingTransaction;
    }

}
