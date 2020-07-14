package ca.bc.gov.open.jag.efilingapi.fee.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FeeRequest Test Suite")
public class FeeRequestTest {

    private static final String DOCTYPE = "DOCTYPE";
    private static final String SUBDOCTYPE = "SUBDOCTYPE";

    @Test
    @DisplayName("CASE1: Test getters")
    public void testGettSetter() {
        FeeRequest testFee = new FeeRequest(DOCTYPE, SUBDOCTYPE);
        Assertions.assertEquals(DOCTYPE, testFee.getDocumentType());
        Assertions.assertEquals(SUBDOCTYPE, testFee.getDocumentSubType());
    }
}
