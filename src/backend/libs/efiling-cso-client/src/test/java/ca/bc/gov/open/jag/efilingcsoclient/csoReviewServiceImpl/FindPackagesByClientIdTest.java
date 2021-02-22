package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.filing.status.FilePackage;
import ca.bc.gov.ag.csows.filing.status.FilingStatus;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingStatusServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Review Service Test Suite")
public class FindPackagesByClientIdTest {
    public static final String CLIENT_FILE_NO = "CLIENTFILENO";
    public static final String COURT_CLASS_CD = "CLASSCD";
    public static final String COURT_FILE_NO = "FILENO";
    public static final String COURT_LEVEL_CD = "LEVELCD";
    public static final String COURT_LOCATION_CD = "LOCATIONCD";
    public static final String COURT_LOCATION_NAME = "LOCATIONAME";
    public static final String FILING_COMMENTS_TXT = "COMMENTSTXT";
    public static final String FIRST_NAME = "FIRSTNAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String PACKAGE_NO = "PACKAGENO";
    public static final DateTime SUBMITED_DATE = new DateTime(2020, 12, 12, 1, 1);

    @Mock
    FilingStatusFacadeBean filingStatusFacadeBean;

    @Mock
    private RestTemplate restTemplateMock;

    private final BigDecimal SUCCESS_CLIENT = BigDecimal.ONE;

    private final BigDecimal EXCEPTION_CLIENT = BigDecimal.TEN;

    private final BigDecimal NOTFOUND_CLIENT = BigDecimal.ZERO;

    private static CsoReviewServiceImpl sut;

    @BeforeAll
    public void beforeAll() throws NestedEjbException_Exception, DatatypeConfigurationException {

        MockitoAnnotations.openMocks(this);

        FilingStatus filingStatus =  createFilingStatus();
        filingStatus.getFilePackages().add(createFilePackage());

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(SUCCESS_CLIENT), any(), any(), any(), any(), any(), any())).thenReturn(filingStatus);

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(NOTFOUND_CLIENT), any(), any(), any(), any(), any(), any())).thenReturn(createFilingStatus());

        Mockito.when(filingStatusFacadeBean.findStatusBySearchCriteria(any(), any(), any(), any(), any(), any(), any(), ArgumentMatchers.eq(EXCEPTION_CLIENT), any(), any(), any(), any(), any(), any())).thenThrow(new NestedEjbException_Exception());

        sut = new CsoReviewServiceImpl(filingStatusFacadeBean, null, null, new FilePackageMapperImpl(), restTemplateMock);
    }

    @DisplayName("OK: packages found")
    @Test
    public void testWithFoundResult() throws DatatypeConfigurationException {
        List<ReviewFilingPackage> result = sut.findStatusByClient(new FilingPackageRequest(SUCCESS_CLIENT, null));

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(COURT_FILE_NO, result.get(0).getCourt().getFileNumber());
        Assertions.assertEquals(COURT_CLASS_CD, result.get(0).getCourt().getCourtClass());
        Assertions.assertEquals(COURT_LEVEL_CD, result.get(0).getCourt().getLevel());
        Assertions.assertEquals(COURT_LOCATION_CD, result.get(0).getCourt().getLocationCd());
        Assertions.assertEquals(COURT_LOCATION_NAME, result.get(0).getCourt().getLocationName());

    }

    @DisplayName("Ok: no packages found")
    @Test
    public void testWithNoResult() {
        List<ReviewFilingPackage> result = sut.findStatusByClient(new FilingPackageRequest(NOTFOUND_CLIENT, null));

        Assertions.assertTrue(result.isEmpty());

    }

    @DisplayName("Exception: filing status facade throws an exception")
    @Test
    public void testWithException() {
        Assertions.assertThrows(EfilingStatusServiceException.class, () -> sut.findStatusByClient(new FilingPackageRequest(EXCEPTION_CLIENT, null)));
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
        filePackage.setSubmittedDate(DateUtils.getXmlDate(SUBMITED_DATE));
        return filePackage;
    }
}
