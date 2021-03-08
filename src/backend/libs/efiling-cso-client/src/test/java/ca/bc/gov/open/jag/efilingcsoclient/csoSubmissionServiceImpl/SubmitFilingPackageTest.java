package ca.bc.gov.open.jag.efilingcsoclient.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.services.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.*;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingPaymentService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.CsoSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.*;
import ca.bc.gov.open.jag.efilingcsoclient.TestHelpers;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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
    private static final String UNIVERSAL_ID = UUID.randomUUID().toString();
    private static final String LOCATION = "LOCATION";
    private static final String BAD_LOCATION = "BADLOCATION";
    private static final String COURT_CLASS = "courtClass";
    private static final String DIVISION = "DIVISION";
    private static final String LEVEL = "level";
    private static final BigDecimal AGENCY_ID = BigDecimal.TEN;
    private static final String FILE_NUMBER = "fileNumber";
    private static final String DOCUMENT = "document";
    private static final String SERVERFILENAME = "serverfilename";
    private static final boolean IS_AMENDMENT = true;
    private static final boolean IS_SUPREME_COURT_SCHEDULING = false;
    private static final String APP_CODE = "APP_CODE";
    private final String TYPE = "type";
    private static final BigDecimal STATUTORY_FEE_AMOUNT = BigDecimal.TEN;

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

        MockitoAnnotations.openMocks(this);

        Mockito.when(csoPropertiesMock.getFileServerHost()).thenReturn("localhost");
        Mockito.when(csoPropertiesMock.getCsoBasePath()).thenReturn("http://cso");
        Mockito.when(csoPropertiesMock.getCsoPackagePath()).thenReturn("/accounts/bceidNotification.do?packageNo=");

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
                null,
                null,
                null));
    }


    @DisplayName("Exception: with null filingPackage should throw IllegalArgumentException")
    @Test
    public void testWithEmptyFilingPackageDetails() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(
                null,
                FilingPackage.builder().create(),
                null));
    }

    @DisplayName("Exception: with null filingpackage should throw IllegalArgumentException")
    @Test
    public void testWithEmptyFilingPackage() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(
                AccountDetails.builder().create(),
                FilingPackage.builder().create()
                , null));
    }


    @DisplayName("Exception: with null clientId should throw IllegalArgumentException")
    @Test
    public void testWithEmptyClientId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(
                AccountDetails.builder().create(),
                FilingPackage.builder().create(),
                null));
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionId() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {

        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .rushedSubmission(false)
                        .court(Court.builder()
                                .location(LOCATION)
                                .agencyId(AGENCY_ID)
                                .courtClass(COURT_CLASS)
                                .division(DIVISION)
                                .level(LEVEL)
                                .fileNumber(FILE_NUMBER)
                                .create())
                        .documents(Arrays.asList(Document.builder()
                                .name(DOCUMENT)
                                .serverFileName(SERVERFILENAME)
                                .isAmendment(IS_AMENDMENT)
                                .isSupremeCourtScheduling(IS_SUPREME_COURT_SCHEDULING)
                                .subType(TYPE)
                                .statutoryFeeAmount(STATUTORY_FEE_AMOUNT)
                                .create()))
                        .create(),
                efilingPaymentServiceMock);

        Mockito.verify(filingFacadeBeanMock, Mockito.times(1))
                .submitFiling(ArgumentMatchers.argThat(filingPackage ->
                        filingPackage.getProcRequest() == null &&
                        filingPackage.getApplicationCd().equals(APP_CODE) &&
                        filingPackage.getCourtFileNo().equals(FILE_NUMBER) &&
                        filingPackage.getLdcxCourtClassCd().equals(COURT_CLASS) &&
                        filingPackage.getLdcxCourtDivisionCd().equals(DIVISION) &&
                        filingPackage.getLdcxCourtLevelCd().equals(LEVEL) &&
                        filingPackage.getSubmittedToAgenId().equals(AGENCY_ID) &&
                        filingPackage.getDocuments().get(0).getPayments().get(0).getStatutoryFeeAmt().equals(STATUTORY_FEE_AMOUNT)));

        Assertions.assertEquals(BigDecimal.TEN, actual.getTransactionId());
        Assertions.assertEquals("http://cso/accounts/bceidNotification.do?packageNo=10", actual.getPackageLink());
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId and rushed flag")
    @Test
    public void withRushedSubmissionShouldAddRushedParams() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {

        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .rushedSubmission(true)
                        .court(Court.builder()
                                .location(LOCATION)
                                .agencyId(AGENCY_ID)
                                .courtClass(COURT_CLASS)
                                .division(DIVISION)
                                .level(LEVEL)
                                .fileNumber(FILE_NUMBER)
                                .create())
                        .documents(Arrays.asList(Document.builder()
                                .name(DOCUMENT)
                                .serverFileName(SERVERFILENAME)
                                .isAmendment(IS_AMENDMENT)
                                .isSupremeCourtScheduling(IS_SUPREME_COURT_SCHEDULING)
                                .subType(TYPE)
                                .statutoryFeeAmount(STATUTORY_FEE_AMOUNT)
                                .create()))
                        .create(),
                efilingPaymentServiceMock);

        Mockito.verify(filingFacadeBeanMock, Mockito.times(1))
                .submitFiling(ArgumentMatchers.argThat(filingPackage ->
                        filingPackage.getProcRequest().getItem().getProcessReasonCd().equals(Keys.RUSH_PROCESS_REASON_CD) &&
                                filingPackage.getProcRequest().getEntDtm().compare(DateUtils.getCurrentXmlDate()) == -1 &&
                                filingPackage.getProcRequest().getEntUserId().equals(accountDetails.getClientId().toString()) &&
                                filingPackage.getProcRequest().getRequestDt().compare(DateUtils.getCurrentXmlDate()) == -1 &&
                                filingPackage.getProcRequest().getItem().getEntDtm().compare(DateUtils.getCurrentXmlDate()) == -1 &&
                                filingPackage.getProcRequest().getItem().getEntUserId().equals(accountDetails.getClientId().toString()) &&
                                filingPackage.getProcRequest().getItem().getProcessReasonCd().equals(Keys.RUSH_PROCESS_REASON_CD) &&
                                filingPackage.getApplicationCd().equals(APP_CODE) &&
                                filingPackage.getCourtFileNo().equals(FILE_NUMBER) &&
                                filingPackage.getLdcxCourtClassCd().equals(COURT_CLASS) &&
                                filingPackage.getLdcxCourtDivisionCd().equals(DIVISION) &&
                                filingPackage.getLdcxCourtLevelCd().equals(LEVEL) &&
                                filingPackage.getSubmittedToAgenId().equals(AGENCY_ID) &&
                                filingPackage.getDocuments().get(0).getPayments().get(0).getStatutoryFeeAmt().equals(STATUTORY_FEE_AMOUNT)));

        Assertions.assertEquals(BigDecimal.TEN, actual.getTransactionId());
        Assertions.assertEquals("http://cso/accounts/bceidNotification.do?packageNo=10", actual.getPackageLink());
    }

    @DisplayName("OK: with no fees should default to 0")
    @Test
    public void withNoFeeShouldDefaultToZero() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {

        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .court(
                                Court.builder()
                                        .location(LOCATION)
                                        .agencyId(AGENCY_ID)
                                        .courtClass(COURT_CLASS)
                                        .division(DIVISION)
                                        .level(LEVEL)
                                        .fileNumber(FILE_NUMBER)
                                        .create())
                        .documents(Arrays.asList(Document.builder()
                                .name(DOCUMENT)
                                .serverFileName(SERVERFILENAME)
                                .isAmendment(IS_AMENDMENT)
                                .isSupremeCourtScheduling(IS_SUPREME_COURT_SCHEDULING)
                                .subType(TYPE)
                                .create()))
                        .parties(Collections.singletonList(
                                Individual.builder()
                                        .firstName("TEST")
                                        .middleName("TEST")
                                        .lastName("TEST")
                                        .nameTypeCd("TYPE")
                                        .roleTypeCd("CODE")
                                        .create()))
                        .organizations(Collections.singletonList(
                                Organization.builder()
                                        .name("TEST")
                                        .nameTypeCd("TYPE")
                                        .roleTypeCd("CODE")
                                        .create()
                        ))
                        .create(),
                efilingPaymentServiceMock);

        Mockito.verify(filingFacadeBeanMock, Mockito.times(1))
                .submitFiling(ArgumentMatchers.argThat(filingPackage ->
                        filingPackage.getCourtFileNo().equals(FILE_NUMBER) &&
                                filingPackage.getLdcxCourtClassCd().equals(COURT_CLASS) &&
                                filingPackage.getLdcxCourtDivisionCd().equals(DIVISION) &&
                                filingPackage.getLdcxCourtLevelCd().equals(LEVEL) &&
                                filingPackage.getSubmittedToAgenId().equals(AGENCY_ID) &&
                                filingPackage.getDocuments().get(0).getPayments().get(0).getStatutoryFeeAmt().equals(BigDecimal.ZERO)));

        Assertions.assertEquals(BigDecimal.TEN, actual.getTransactionId());
        Assertions.assertEquals("http://cso/accounts/bceidNotification.do?packageNo=10", actual.getPackageLink());
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionIdBigDecimalValue() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {

        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(new BigDecimal(100000));
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .court(
                                Court.builder()
                                        .location(LOCATION)
                                        .create())
                        .create(),
                efilingPaymentServiceMock);
        Assertions.assertEquals(new BigDecimal(100000), actual.getTransactionId());
        Assertions.assertEquals("http://cso/accounts/bceidNotification.do?packageNo=100000", actual.getPackageLink());

    }

    @DisplayName("OK: submitFilingPackage called with rushed Processing")
    @Test
    public void testWithRushedProcessing() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenReturn(createTransaction());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        AccountDetails accountDetails = getAccountDetails();

        SubmitPackageResponse actual = sut.submitFilingPackage(accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .court(
                                Court.builder()
                                        .location(LOCATION)
                                        .create())
                        .create(),
                efilingPaymentServiceMock);

        Assertions.assertEquals(BigDecimal.TEN, actual.getTransactionId());
        Assertions.assertEquals("http://cso/accounts/bceidNotification.do?packageNo=10", actual.getPackageLink());
    }

    @DisplayName("Exception: payment to bambora throw exception")
    @Test
    public void testWithValidRequestPaymentThrowsException() throws DatatypeConfigurationException, ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(efilingPaymentServiceMock.makePayment(any())).thenThrow(new EfilingSubmissionServiceException("Bad Bambora", new Throwable()));

        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
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
                accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .court(
                                Court.builder()
                                        .location(LOCATION)
                                        .create())
                        .create(),
                efilingPaymentServiceMock));

    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenServiceFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws NestedEjbException_Exception {
        Mockito.when(serviceFacadeBean.addService(any())).thenThrow(new NestedEjbException_Exception());

        AccountDetails accountDetails = getAccountDetails();

        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                accountDetails,
                FilingPackage
                        .builder()
                        .applicationCode(APP_CODE)
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
                getAccountDetails(),
                FilingPackage
                        .builder()
                        .applicationCode(APP_CODE)
                        .create(),
                null));
    }

    @DisplayName("Exception: with NestedEjbException_Exception when getting user session should throw EfilingLookupServiceException")
    @Test
    public void whenCreateUserSessionThrowsNestedEjbException_Exception() throws NestedEjbException_Exception {
        Mockito.doThrow(new NestedEjbException_Exception()).when(serviceFacadeBean).createUserSession(Mockito.anyString());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(
                getAccountDetails(),
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
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
                accountDetails,
                FilingPackage
                        .builder()
                        .applicationCode(APP_CODE)
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
                accountDetails,
                FilingPackage.builder()
                        .applicationCode(APP_CODE)
                        .court(
                                Court.builder()
                                        .location(BAD_LOCATION)
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
