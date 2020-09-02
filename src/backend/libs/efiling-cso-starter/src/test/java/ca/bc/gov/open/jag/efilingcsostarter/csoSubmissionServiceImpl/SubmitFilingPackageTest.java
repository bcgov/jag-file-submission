package ca.bc.gov.open.jag.efilingcsostarter.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsostarter.CsoSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcsostarter.TestHelpers;
import ca.bc.gov.open.jag.efilingcsostarter.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.*;
import org.joda.time.DateTime;
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

    private static final String INTERNAL_CLIENT_NUMBER = "internalClientNumber";
    private static final String CARD_TYPE_CD = "V";
    private static final String REFERENCE_MESSAGE_TXT = "message";
    private static final String TRANSACTION_STATE_CD = "12";
    private static final String APPROVAL_CD = "APP";
    private static final String INVOICE_NO = "1234";
    private static final String USER_ID = "userId";
    private static final BigDecimal SERVICE_ID1 = new BigDecimal(5);
    private static final BigDecimal TRANSACTION_AMT = new BigDecimal(2);
    private static final DateTime TRANSACTON_DTM = DateTime.now();
    private static final DateTime PROCESS_DT = DateTime.now();
    private static final DateTime ENT_DTM = DateTime.now();
    private static final String APPROVED = "Approved";
    public static final UUID UNIVERSAL_ID = UUID.randomUUID();
    private static final String LOCATION = "LOCATION";
    private static final String BAD_LOCATION = "BADLOCATION";
    public static final String APP_CODE = "APP_CODE";
    CsoSubmissionServiceImpl sut;

    @Mock
    private FilingFacadeBean filingFacadeBeanMock;

    @Mock
    private ServiceFacadeBean serviceFacadeBean;

    @Mock
    private EfilingPaymentService efilingPaymentServiceMock;

    @Mock
    private CsoProperties csoPropertiesMock;

    @BeforeEach
    public void init() throws NestedEjbException_Exception, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        Mockito.when(csoPropertiesMock.getFileServerHost()).thenReturn("localhost");
        Mockito.when(csoPropertiesMock.getCsoBasePath()).thenReturn("http://cso");

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

        // Testing mapper
        DocumentMapper documentMapper = new DocumentMapperImpl();
        CsoPartyMapper csoPartyMapper = new CsoPartyMapperImpl();

        Mockito.doReturn(DateUtils.getCurrentXmlDate()).when(filingFacadeBeanMock)
                .calculateSubmittedDate(any(), Mockito.eq(LOCATION));

        Mockito.doThrow(ca.bc.gov.ag.csows.filing.NestedEjbException_Exception.class).when(filingFacadeBeanMock)
                .calculateSubmittedDate(any(), Mockito.eq(BAD_LOCATION));

        sut = new CsoSubmissionServiceImpl(filingFacadeBeanMock, serviceFacadeBean, new ServiceMapperImpl(), new FilingPackageMapperImpl(), new FinancialTransactionMapperImpl(), csoPropertiesMock, documentMapper, csoPartyMapper, new PackageAuthorityMapperImpl());

    }


    @DisplayName("Exception: with null account details service should throw IllegalArgumentException")
    @Test
    public void testWithEmptyAccountDetails() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest.builder().create(),
                null));
    }


    @DisplayName("Exception: with null filingPackage should throw IllegalArgumentException")
    @Test
    public void testWithEmptyFilingPackage() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest.builder().accountDetails(AccountDetails.builder().clientId(BigDecimal.TEN).create()).create(),
                null));
    }

    @DisplayName("Exception: with null clientID should throw IllegalArgumentException")
    @Test
    public void testWithNoClientId() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest.builder()
                        .accountDetails(AccountDetails.builder().create())
                        .filingPackage(FilingPackage.builder().create()).create(), null));
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionId() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(
                SubmitPackageRequest.builder()
                        .accountDetails(accountDetails)
                        .filingPackage(FilingPackage.builder()
                                .court(Court.builder()
                                        .location(LOCATION)
                                        .create())
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                efilingPaymentServiceMock);
        Assertions.assertEquals(BigDecimal.TEN, actual.getTransactionId());
        Assertions.assertEquals("http://cso/cso/accounts/bceidNotification.do?packageNo=10", actual.getPackageLink());
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionIdBigDecimalValue() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {

        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(new BigDecimal(100000));
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(
                SubmitPackageRequest.builder()
                        .accountDetails(accountDetails)
                        .filingPackage(FilingPackage
                                .builder()
                                .court(Court.builder()
                                        .location(LOCATION)
                                        .create())
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                efilingPaymentServiceMock);
        Assertions.assertEquals(new BigDecimal(100000), actual.getTransactionId());
        Assertions.assertEquals("http://cso/cso/accounts/bceidNotification.do?packageNo=100000", actual.getPackageLink());

    }

    @DisplayName("OK: submitFilingPackage called with rushed Processing")
    @Test
    public void testWithRushedProcessing() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(accountDetails)
                        .filingPackage(
                                FilingPackage.builder()
                                        .court(
                                                Court.builder()
                                                        .location(LOCATION)
                                                        .create())
                                        .rushedSubmission(true)
                                        .applicationType(APP_CODE)
                                        .create())
                        .create(),
                efilingPaymentServiceMock);

        Assertions.assertEquals(BigDecimal.TEN, actual.getTransactionId());
        Assertions.assertEquals("http://cso/cso/accounts/bceidNotification.do?packageNo=10", actual.getPackageLink());
    }

    @DisplayName("Exception: payment to bambora throw exception")
    @Test
    public void testWithValidRequestPaymentThrowsException() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenThrow(new EfilingSubmissionServiceException("Bad Bambora", new Throwable()));

        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(accountDetails)
                        .filingPackage(FilingPackage.builder()
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                efilingPaymentServiceMock));
    }


    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenFilingFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws NestedEjbException_Exception, DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception {

        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenThrow(new ca.bc.gov.ag.csows.filing.NestedEjbException_Exception());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(accountDetails)
                        .filingPackage(FilingPackage
                                .builder()
                                .court(Court
                                        .builder()
                                        .location(LOCATION)
                                        .create())
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                efilingPaymentServiceMock));

    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenServiceFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws NestedEjbException_Exception {
        Mockito.when(serviceFacadeBean.addService(any())).thenThrow(new NestedEjbException_Exception());

        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () ->
                sut.submitFilingPackage(
                        SubmitPackageRequest
                                .builder()
                                .accountDetails(accountDetails)
                                .filingPackage(FilingPackage
                                        .builder()
                                        .applicationType(APP_CODE)
                                        .create())
                                .create(),
                        null));
    }

    private AccountDetails getAccountDetails() {
        return AccountDetails
                .builder()
                .clientId(BigDecimal.TEN)
                .internalClientNumber(INTERNAL_CLIENT_NUMBER)
                .universalId(UNIVERSAL_ID)
                .cardRegistered(true)
                .accountId(BigDecimal.ONE)
                .fileRolePresent(true)
                .create();
    }


    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenCreateServiceSessionThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {

        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).createServiceSession(Mockito.any(), Mockito.anyString());

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(getAccountDetails())
                        .filingPackage(FilingPackage
                                .builder()
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                null));
    }

    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenCreateUserSessionThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {
        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).createUserSession(Mockito.anyString());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(getAccountDetails())
                .filingPackage(FilingPackage
                                .builder()
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                null));
    }


    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenUpdatePaymentForServiceThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {

        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).updateService(ArgumentMatchers.argThat(service -> service.getFeePaidYn().equals(String.valueOf(true))));
        Service service = new Service();
        Mockito.when(serviceFacadeBean.addService(Mockito.any())).thenReturn(service);

        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(accountDetails)
                    .filingPackage(FilingPackage
                                    .builder()
                            .applicationType(APP_CODE)
                                    .create())
                        .create(),
                efilingPaymentServiceMock));

    }

    @DisplayName("Exception: with NestedEjbException_Exception on calculate submitted date throw EfilingLookupServiceException")
    @Test
    public void whenComputedSubmittedDateFailsThrowEfilingLookupServiceException() throws NestedEjbException_Exception, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, DatatypeConfigurationException {

        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenThrow(new ca.bc.gov.ag.csows.filing.NestedEjbException_Exception());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());

        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                SubmitPackageRequest
                        .builder()
                        .accountDetails(accountDetails)
                        .filingPackage(FilingPackage
                                .builder()
                                .court(Court.builder()
                                        .location(LOCATION).create())
                                .applicationType(APP_CODE)
                                .create())
                        .create(),
                efilingPaymentServiceMock));

    }


    private PaymentTransaction createTransaction() {
        PaymentTransaction efilingTransaction = new PaymentTransaction();
        efilingTransaction.setApprovalCd(APPROVED);
        efilingTransaction.setEcommerceTransactionId(BigDecimal.TEN);
        efilingTransaction.setInternalClientNo(INTERNAL_CLIENT_NUMBER);
        efilingTransaction.setCreditCardTypeCd(CARD_TYPE_CD);
        efilingTransaction.setReferenceMessageTxt(REFERENCE_MESSAGE_TXT);
        efilingTransaction.setTransactionStateCd(TRANSACTION_STATE_CD);
        efilingTransaction.setApprovalCd(APPROVAL_CD);
        efilingTransaction.setEntDtm(ENT_DTM);
        efilingTransaction.setInvoiceNo(INVOICE_NO);
        efilingTransaction.setProcessDt(PROCESS_DT);
        efilingTransaction.setTransactionAmt(TRANSACTION_AMT);
        efilingTransaction.setTransactonDtm(TRANSACTON_DTM);
        efilingTransaction.setEntUserId(USER_ID);
        efilingTransaction.setServiceId(SERVICE_ID1);
        return efilingTransaction;
    }

}
