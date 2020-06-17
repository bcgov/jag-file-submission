package ca.bc.gov.open.jag.efilingworker;

import ca.bc.gov.open.jag.efilinglookupclient.MockCSOLookupServiceImpl;
import ca.bc.gov.open.jag.efilingworker.service.MockDocumentStoreServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("EfilingMessageConsumer Test Suite")
public class EfilingMessageConsumerTest {
    private static final String TEST = "TEST";
    @InjectMocks
    EfilingMessageConsumer sut;

    @Mock
    MockDocumentStoreServiceImpl mockDocumentStoreService;

    @Mock
    MockCSOLookupServiceImpl mockCSOLookupService;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockDocumentStoreService.uploadFile(any())).thenReturn(TEST);
    }

    @DisplayName("CASE1: Test acceptMessage execution")
    @Test
    public void test() {
        sut.acceptMessage("TEST");
        verify(mockDocumentStoreService, times(1)).uploadFile(any());
    }
}
