package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.filing.status.*;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.LookupItem;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Review Service Test Suite")
public class FindPackageByIdTest {
    private static final String CLIENT_FILE_NO = "CLIENTFILENO";
    private static final String COURT_CLASS_CD = "CLASSCD";
    private static final String COURT_FILE_NO = "FILENO";
    private static final String COURT_LEVEL_CD = "LEVELCD";
    private static final String COURT_LOCATION_CD = "LOCATIONCD";
    private static final String COURT_LOCATION_NAME = "LOCATIONAME";
    private static final String FILING_COMMENTS_TXT = "COMMENTSTXT";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String PACKAGE_NO = "PACKAGENO";
    private static final String REASON_TXT = "ReasonTxt";
    private static final DateTime SUBMITED_DATE = new DateTime(2020, 12, 12, 1, 1);
    private static final String EMAIL = "EMAIL";
    private static final String PHONE = "PHONE";
    private static final String COUNTRY_ONE = "COUNTRY ONE";
    private static final String COUNTRY_TEN = "COUNTRY TEN";
    private static final String PROCESSING_COMMENT_TXT = "PROCESSTEXT";
    private static final String FILE_GUID = UUID.randomUUID().toString();
    private static final String FILE_PDF = "FILE.pdf";

    @Mock
    FilingStatusFacadeBean filingStatusFacadeBean;

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private EfilingLookupService efilingLookupService;

    private final BigDecimal SUCCESS_CLIENT = BigDecimal.ONE;

    private final BigDecimal SUCCESS_PACKAGE = BigDecimal.ONE;

    private final BigDecimal SUCCESS_REJECTED_CLIENT = BigDecimal.valueOf(11);
    private final BigDecimal SUCCESS_REJECTED_PACKAGE = BigDecimal.valueOf(11);

    private final BigDecimal SUCCESS_COMPLETE_CLIENT = BigDecimal.valueOf(12);
    private final BigDecimal SUCCESS_COMPLETE_PACKAGE = BigDecimal.valueOf(12);

    private final BigDecimal EXCEPTION_CLIENT = BigDecimal.TEN;
    private final BigDecimal EXCEPTION_PACKAGE = BigDecimal.TEN;

    private final BigDecimal NOTFOUND_CLIENT = BigDecimal.ZERO;
    private final BigDecimal NOTFOUND_PACKAGE = BigDecimal.ZERO;

    private final BigDecimal ACCOUNT = BigDecimal.ONE;

    private static CsoReviewServiceImpl sut;


    @BeforeAll
    public void beforeAll() throws NestedEjbException_Exception, DatatypeConfigurationException {

        MockitoAnnotations.openMocks(this);

        FilingStatus filingStatus =  createFilingStatus();
        FilePackage pendingFilePackage = createFilePackage();
        File documentPending = new File();
        documentPending.setStatus(Keys.CSO_DOCUMENT_REFERRED);
        pendingFilePackage.getFiles().add(documentPending);
        filingStatus.getFilePackages().add(pendingFilePackage);

        FilingStatus filingRejectedStatus =  createFilingStatus();
        FilePackage rejectedFilePackage = createFilePackage();
        File documentRejected = new File();
        documentRejected.setStatus(Keys.CSO_DOCUMENT_REJECTED);
        rejectedFilePackage.getFiles().add(documentRejected);
        filingRejectedStatus.getFilePackages().add(rejectedFilePackage);

        FilingStatus filingCompleteStatus =  createFilingStatus();
        FilePackage completeFilePackage = createFilePackage();
        File documentComplete = new File();
        documentComplete.setStatus("COMPLETE");
        rejectedFilePackage.getFiles().add(documentComplete);
        filingCompleteStatus.getFilePackages().add(completeFilePackage);

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(SUCCESS_PACKAGE), ArgumentMatchers.eq(SUCCESS_CLIENT), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(filingStatus);

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(SUCCESS_REJECTED_PACKAGE), ArgumentMatchers.eq(SUCCESS_REJECTED_CLIENT), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(filingRejectedStatus);

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(SUCCESS_COMPLETE_PACKAGE), ArgumentMatchers.eq(SUCCESS_COMPLETE_CLIENT), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(filingCompleteStatus);

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(NOTFOUND_PACKAGE), ArgumentMatchers.eq(NOTFOUND_CLIENT), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(createFilingStatus());

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(),  any(), any(), any(), any(), any(), ArgumentMatchers.eq(EXCEPTION_PACKAGE), ArgumentMatchers.eq(EXCEPTION_CLIENT), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenThrow(new NestedEjbException_Exception());

        Mockito.when(efilingLookupService.getCountries()).thenReturn(createCountryList());

        CsoProperties csoProperties = new CsoProperties();
        csoProperties.setCsoBasePath("http://locahost:8080");

        sut = new CsoReviewServiceImpl(filingStatusFacadeBean, null, null, new FilePackageMapperImpl(), csoProperties, restTemplateMock, efilingLookupService);
    }

    @DisplayName("OK: package found pending status")
    @Test
    public void testWithFoundResult() throws DatatypeConfigurationException {

        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(SUCCESS_CLIENT, ACCOUNT, SUCCESS_PACKAGE, null));

        Assertions.assertEquals(COURT_FILE_NO, result.get().getCourt().getFileNumber());
        Assertions.assertEquals(COURT_CLASS_CD, result.get().getCourt().getCourtClass());
        Assertions.assertEquals(COURT_LEVEL_CD, result.get().getCourt().getLevel());
        Assertions.assertEquals(COURT_LOCATION_CD, result.get().getCourt().getLocationCd());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get().getCourt().getLocationName());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get().getCourt().getLocationName());
        Assertions.assertEquals(REASON_TXT, result.get().getRushOrder().getRushFilingReasonTxt());
        Assertions.assertEquals(COUNTRY_ONE, result.get().getRushOrder().getCountryDsc());
        Assertions.assertEquals(EMAIL, result.get().getRushOrder().getContactEmailTxt());
        Assertions.assertEquals(FIRST_NAME, result.get().getRushOrder().getContactFirstGivenNm());
        Assertions.assertEquals(PHONE, result.get().getRushOrder().getContactPhoneNo());
        Assertions.assertEquals(LAST_NAME, result.get().getRushOrder().getContactSurnameNm());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getPackageId());
        Assertions.assertEquals(PROCESSING_COMMENT_TXT, result.get().getRushOrder().getProcessingCommentTxt());
        Assertions.assertEquals(1, result.get().getRushOrder().getSupportDocs().size());
        Assertions.assertEquals(FILE_GUID, result.get().getRushOrder().getSupportDocs().get(0).getObjectGuid());
        Assertions.assertEquals(FILE_PDF, result.get().getRushOrder().getSupportDocs().get(0).getClientFileNm());
        Assertions.assertEquals(Keys.PACKAGE_STATUS_PENDING, result.get().getStatus());

    }

    @DisplayName("OK: package found rejected status")
    @Test
    public void testWithFoundResultRejected() throws DatatypeConfigurationException {

        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(SUCCESS_REJECTED_CLIENT, ACCOUNT, SUCCESS_REJECTED_PACKAGE, null));

        Assertions.assertEquals(COURT_FILE_NO, result.get().getCourt().getFileNumber());
        Assertions.assertEquals(COURT_CLASS_CD, result.get().getCourt().getCourtClass());
        Assertions.assertEquals(COURT_LEVEL_CD, result.get().getCourt().getLevel());
        Assertions.assertEquals(COURT_LOCATION_CD, result.get().getCourt().getLocationCd());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get().getCourt().getLocationName());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get().getCourt().getLocationName());
        Assertions.assertEquals(REASON_TXT, result.get().getRushOrder().getRushFilingReasonTxt());
        Assertions.assertEquals(COUNTRY_ONE, result.get().getRushOrder().getCountryDsc());
        Assertions.assertEquals(EMAIL, result.get().getRushOrder().getContactEmailTxt());
        Assertions.assertEquals(FIRST_NAME, result.get().getRushOrder().getContactFirstGivenNm());
        Assertions.assertEquals(PHONE, result.get().getRushOrder().getContactPhoneNo());
        Assertions.assertEquals(LAST_NAME, result.get().getRushOrder().getContactSurnameNm());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getPackageId());
        Assertions.assertEquals(PROCESSING_COMMENT_TXT, result.get().getRushOrder().getProcessingCommentTxt());
        Assertions.assertEquals(1, result.get().getRushOrder().getSupportDocs().size());
        Assertions.assertEquals(FILE_GUID, result.get().getRushOrder().getSupportDocs().get(0).getObjectGuid());
        Assertions.assertEquals(FILE_PDF, result.get().getRushOrder().getSupportDocs().get(0).getClientFileNm());
        Assertions.assertEquals(Keys.PACKAGE_STATUS_ACTION_REQUIRED, result.get().getStatus());

    }

    @DisplayName("OK: package found complete status")
    @Test
    public void testWithFoundResultComplete() throws DatatypeConfigurationException {

        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(SUCCESS_COMPLETE_CLIENT, ACCOUNT, SUCCESS_COMPLETE_PACKAGE, null));

        Assertions.assertEquals(COURT_FILE_NO, result.get().getCourt().getFileNumber());
        Assertions.assertEquals(COURT_CLASS_CD, result.get().getCourt().getCourtClass());
        Assertions.assertEquals(COURT_LEVEL_CD, result.get().getCourt().getLevel());
        Assertions.assertEquals(COURT_LOCATION_CD, result.get().getCourt().getLocationCd());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get().getCourt().getLocationName());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get().getCourt().getLocationName());
        Assertions.assertEquals(REASON_TXT, result.get().getRushOrder().getRushFilingReasonTxt());
        Assertions.assertEquals(COUNTRY_ONE, result.get().getRushOrder().getCountryDsc());
        Assertions.assertEquals(EMAIL, result.get().getRushOrder().getContactEmailTxt());
        Assertions.assertEquals(FIRST_NAME, result.get().getRushOrder().getContactFirstGivenNm());
        Assertions.assertEquals(PHONE, result.get().getRushOrder().getContactPhoneNo());
        Assertions.assertEquals(LAST_NAME, result.get().getRushOrder().getContactSurnameNm());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getPackageId());
        Assertions.assertEquals(PROCESSING_COMMENT_TXT, result.get().getRushOrder().getProcessingCommentTxt());
        Assertions.assertEquals(1, result.get().getRushOrder().getSupportDocs().size());
        Assertions.assertEquals(FILE_GUID, result.get().getRushOrder().getSupportDocs().get(0).getObjectGuid());
        Assertions.assertEquals(FILE_PDF, result.get().getRushOrder().getSupportDocs().get(0).getClientFileNm());
        Assertions.assertEquals(Keys.PACKAGE_STATUS_COMPLETE, result.get().getStatus());

    }

    @DisplayName("Ok: no packages found")
    @Test
    public void testWithNoResult() {

        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(NOTFOUND_CLIENT, ACCOUNT, NOTFOUND_PACKAGE, null));
        Assertions.assertFalse(result.isPresent());

    }

    @DisplayName("Exception: filing status facade throws an exception")
    @Test
    public void testWithException() {
        Assertions.assertThrows(EfilingStatusServiceException.class, () -> sut.findStatusByPackage(new FilingPackageRequest(EXCEPTION_CLIENT, ACCOUNT, EXCEPTION_PACKAGE, null)));
    }

    private FilingStatus createFilingStatus() {
        FilingStatus filingStatus = new FilingStatus();
        filingStatus.setRecordsFrom(BigDecimal.ONE);
        filingStatus.setRecordsTo(BigDecimal.ONE);
        filingStatus.setTotalRecords(BigDecimal.ONE);
        return filingStatus;
    }

    private FilePackage createFilePackage() throws DatatypeConfigurationException {
        FilePackage filePackage = new FilePackage();
        filePackage.setClientFileNo(CLIENT_FILE_NO);
        filePackage.setCourtClassCd(COURT_CLASS_CD);
        filePackage.setCourtFileNo(COURT_FILE_NO);
        filePackage.setCourtLevelCd(COURT_LEVEL_CD);
        filePackage.setCourtLocationCd(COURT_LOCATION_CD);
        filePackage.setCourtLocationId(BigDecimal.ONE);
        filePackage.setCourtLocationName(COURT_LOCATION_NAME);
        filePackage.setExistingCourtFileYN(true);
        filePackage.setFilingCommentsTxt(FILING_COMMENTS_TXT);
        filePackage.setFirstName(FIRST_NAME);
        filePackage.setHasChecklist(true);
        filePackage.setHasRegistryNotice(true);
        filePackage.setLastName(LAST_NAME);
        filePackage.setPackageNo(PACKAGE_NO);

        filePackage.setProcRequest(createRushOrderRequest());
        filePackage.setSubmittedDate(DateUtils.getXmlDate(SUBMITED_DATE));
        return filePackage;
    }

    private RushOrderRequest createRushOrderRequest() {
        RushOrderRequest rushOrderRequest = new RushOrderRequest();

        rushOrderRequest.setContactEmailTxt(EMAIL);
        rushOrderRequest.setContactFirstGivenNm(FIRST_NAME);
        rushOrderRequest.setContactPhoneNo(PHONE);
        rushOrderRequest.setContactSurnameNm(LAST_NAME);
        rushOrderRequest.setCtryId(BigDecimal.ONE);
        rushOrderRequest.setProcessingCommentTxt(PROCESSING_COMMENT_TXT);

        RushOrderRequestItem rushOrderRequestItem = new RushOrderRequestItem();

        rushOrderRequestItem.setPackageId(BigDecimal.ONE);
        rushOrderRequestItem.setRushFilingReasonTxt(REASON_TXT);

        ProcessSupportDocument processSupportDocument = new ProcessSupportDocument();
        processSupportDocument.setClientFileNm(FILE_PDF);
        processSupportDocument.setObjectGuid(FILE_GUID);
        rushOrderRequestItem.getSupportDocs().add(processSupportDocument);

        rushOrderRequest.setItem(rushOrderRequestItem);

        return rushOrderRequest;
    }

    private List<LookupItem> createCountryList() {
        return Arrays.asList(LookupItem.builder()
                .code(BigDecimal.ONE.toEngineeringString())
                .description(COUNTRY_ONE)
                .create(),
                LookupItem.builder()
                .code(BigDecimal.TEN.toEngineeringString())
                .description(COUNTRY_TEN)
                .create());
    }

}
