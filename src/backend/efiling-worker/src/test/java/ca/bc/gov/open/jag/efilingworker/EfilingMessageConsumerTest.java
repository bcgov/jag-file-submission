package ca.bc.gov.open.jag.efilingworker;

import ca.bc.gov.open.jag.efilinglookupclient.MockCSOLookupServiceImpl;
import ca.bc.gov.open.jag.efilingworker.service.MockDocumentStoreServiceImpl;
import org.junit.jupiter.api.Test;

public class EfilingMessageConsumerTest {
    @Test
    public void test() {
        EfilingMessageConsumer efilingMessageConsumer = new EfilingMessageConsumer(new MockDocumentStoreServiceImpl(), new MockCSOLookupServiceImpl());
        efilingMessageConsumer.acceptMessage("TEST");
    }
}
