package ca.bc.gov.open.jag.efilingreviewerapi.extract.models;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExtractTest {

    private Extract sut;

    @Test
    @DisplayName("when extract is processed should return processed time")
    public void whenExtractIsProcessedShouldReturnProcessedTime() {

        ExtractRequest sut = new ExtractRequest(
                Extract.builder().id(UUID.randomUUID()).transactionId(UUID.randomUUID()).create(),
                Document.builder().fileName("test").create(), 1l, 3l);

        Assertions.assertEquals(2l, sut.getProcessingTimeMillis());

    }

}

