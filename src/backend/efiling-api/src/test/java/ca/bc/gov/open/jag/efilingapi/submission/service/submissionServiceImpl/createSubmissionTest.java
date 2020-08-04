package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.api.model.CreateServiceResponse;
import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.models.Submission;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class createSubmissionTest {

    private SubmissionServiceImpl sut;

    @Mock
    private SubmissionStore submissionStoreMock;

    @Mock
    private CacheProperties cachePropertiesMock;

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
        Mockito.when(efilingSubmissionServiceMock.addService(any())).thenReturn(TestHelpers.createEfilingService());
        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, documentStoreMock);

    }


    @Test
    @DisplayName("OK: service is created")
    public void withValidSubmissionServiceIsCreated() {

        CreateServiceResponse actual = sut.createSubmission(Submission
                .builder()
                .id(TestHelpers.CASE_1)
                .clientId(BigDecimal.TEN)
                .accountId(BigDecimal.TEN)
                .transactionId(TestHelpers.CASE_1)
                .navigation(TestHelpers.createDefaultNavigation())
                .expiryDate(10)
                .clientApplication(TestHelpers.createClientApplication(TestHelpers.DISPLAY_NAME, TestHelpers.TYPE))
                .filingPackage(TestHelpers.createPackage(TestHelpers.createCourt(), TestHelpers.createDocumentList()))
                .create());
        assertEquals(BigDecimal.TEN, actual.getServiceId());
    }

}
