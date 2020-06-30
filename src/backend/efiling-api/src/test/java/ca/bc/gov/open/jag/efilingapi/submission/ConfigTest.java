package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStore;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigTest {

    private SubmissionConfig sut;

    @Mock
    CacheManager cacheManagerMock;

    @BeforeAll
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        sut = new SubmissionConfig();
    }

    @Test
    @DisplayName("CASE 1: returns the SubmissionStore")
    public void testGetSubmissionService()  {

        SubmissionStore actual = sut.submissionStore();
        Assertions.assertEquals(SubmissionStoreImpl.class, actual.getClass());

    }
}
