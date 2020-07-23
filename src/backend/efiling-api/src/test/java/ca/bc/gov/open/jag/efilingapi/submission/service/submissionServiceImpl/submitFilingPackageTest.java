package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmitFilingPackageResponse;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.exceptions.InvalidAccountStateException;
import ca.bc.gov.open.jag.efilingcommons.exceptions.StoreException;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingAccountService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class submitFilingPackageTest {

    private SubmissionServiceImpl sut;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private CacheProperties cachePropertiesMock;

    @Mock
    private EfilingAccountService efilingAccountServiceMock;

    @Mock
    private EfilingLookupService efilingLookupService;

    @Mock
    private EfilingCourtService efilingCourtService;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private EfilingSubmissionService efilingSubmissionServiceMock;

    @BeforeAll
    public void setUp(){

        MockitoAnnotations.initMocks(this);
        Mockito.when(efilingSubmissionServiceMock.submitFilingPackage(any())).thenReturn(BigDecimal.TEN);
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, efilingAccountServiceMock, efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock);

    }


    @Test
    @DisplayName("OK: with valid account should return submission")
    public void withValidAccountShouldReturnSubmission() {

        SubmitFilingPackageResponse actual = sut.submitFilingPackage(UUID.randomUUID(), TestHelpers.CASE_2, new SubmitFilingPackageRequest());
        assertEquals(BigDecimal.TEN, actual.getTransactionId());
        assertEquals(LocalDate.now().getDayOfMonth(), actual.getAcknowledge().getDayOfMonth());
        assertEquals(LocalDate.now().getDayOfYear(), actual.getAcknowledge().getDayOfYear());
        assertEquals(LocalDate.now().getYear(), actual.getAcknowledge().getYear());
    }

}
