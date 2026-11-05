package ca.bc.gov.open.jag.efilingapi.submission.service.submissionServiceImpl;

import ca.bc.gov.open.jag.efilingapi.document.DocumentStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingLookupService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IsRushRequiredTest {

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
    private EfilingDocumentService efilingDocumentService;

    @Mock
    private DocumentStore documentStoreMock;

    @Mock
    private EfilingSubmissionService efilingSubmissionServiceMock;

    @BeforeAll
    public void beforeAll() throws DatatypeConfigurationException {

        MockitoAnnotations.openMocks(this);

        sut = new SubmissionServiceImpl(submissionStoreMock, cachePropertiesMock, null, null, efilingLookupService, efilingCourtService, efilingSubmissionServiceMock, efilingDocumentService, documentStoreMock, null, null, null);

    }

    @Test
    @DisplayName("Test is rushRequired")
    public void testRushRequired() {

        Mockito.when(efilingDocumentService.getDocumentTypeDetails(any(), any(), any(), any())).thenReturn(new DocumentTypeDetails("description",
                "type",
                BigDecimal.TEN,
                true,
                true,
                true));

        Assertions.assertTrue(sut.isRushRequired("TEST","TEST","TEST"));

    }

    @Test
    @DisplayName("Test is not rushRequired")
    public void testRushNotRequired() {

        Mockito.when(efilingDocumentService.getDocumentTypeDetails(any(), any(), any(), any())).thenReturn(new DocumentTypeDetails("description",
                "type",
                BigDecimal.TEN,
                true,
                false,
                true));

        Assertions.assertFalse(sut.isRushRequired("TEST","TEST","TEST"));

    }

}
