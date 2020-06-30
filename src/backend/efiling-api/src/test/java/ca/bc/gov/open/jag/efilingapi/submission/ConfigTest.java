package ca.bc.gov.open.jag.efilingapi.submission;

import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapper;
import ca.bc.gov.open.jag.efilingapi.submission.mappers.SubmissionMapperImpl;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionServiceImpl;
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

        sut = new SubmissionConfig(null);
    }

    @Test
    @DisplayName("get SubmissionStoreImpl test")
    public void testGetSubmissionStore()  {

        SubmissionStore actual = sut.submissionStore();
        Assertions.assertEquals(SubmissionStoreImpl.class, actual.getClass());

    }

    @Test
    @DisplayName("get SubmissionServiceImpl test")
    public void testGetSubmissionService() {

        SubmissionService actual = sut.submissionService(null, null, null, null);
        Assertions.assertEquals(SubmissionServiceImpl.class, actual.getClass());

    }

    @Test
    @DisplayName("get SubmissionMapperImpl test")
    public void testGetSubmissionMapper() {

        SubmissionMapper actual = sut.submissionMapper();
        Assertions.assertEquals(SubmissionMapperImpl.class, actual.getClass());

    }


}
