package ca.bc.gov.open.jag.efilingapi.submission.service.submissionStore;

import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteByKeyTest {

    @InjectMocks
    private SubmissionStoreImpl sut;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("OK: evict should do nothing")
    public void testEvict() {
        Assertions.assertDoesNotThrow(() -> sut.evict(new SubmissionKey( UUID.randomUUID().toString(), UUID.randomUUID(), UUID.randomUUID())));
    }

}
