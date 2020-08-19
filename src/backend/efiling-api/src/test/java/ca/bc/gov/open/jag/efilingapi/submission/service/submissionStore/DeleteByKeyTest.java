package ca.bc.gov.open.jag.efilingapi.submission.service.submissionStore;

import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionStoreImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteByKeyTest {

    @InjectMocks
    private SubmissionStoreImpl sut;


    @Test
    @DisplayName("OK: evict should do nothing")
    public void testEvict() {
        Assertions.assertDoesNotThrow(() -> sut.evict(UUID.randomUUID(), UUID.randomUUID()));
    }

}
