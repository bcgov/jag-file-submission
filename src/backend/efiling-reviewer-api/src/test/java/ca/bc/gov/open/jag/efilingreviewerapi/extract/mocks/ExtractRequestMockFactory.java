package ca.bc.gov.open.jag.efilingreviewerapi.extract.mocks;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.Extract;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;

import java.math.BigDecimal;
import java.util.UUID;

public class ExtractRequestMockFactory {

    public static final String EXPECTED_DOCUMENT_CONTENT_TYPE = "application/pdf";
    public static final String EXPECTED_DOCUMENT_FILE_NAME = "myfile.pdf";
    public static final String EXPECTED_DOCUMENT_TYPE = "type";
    public static final BigDecimal EXPECTED_DOCUMENT_SIZE = BigDecimal.TEN;
    public static final UUID EXPECTED_EXTRACT_ID = UUID.fromString("89714a2a-611e-4f36-811b-1b2eb4cce7ee");
    public static final UUID EXPECTED_EXTRACT_TRANSACTION_ID = UUID.fromString("e40fb65d-1b61-40b9-9deb-d8eb3bab877f");

    public static ExtractRequest mock() {

        return new ExtractRequest(
                new Extract(EXPECTED_EXTRACT_ID, EXPECTED_EXTRACT_TRANSACTION_ID, true),
                new Document(EXPECTED_DOCUMENT_TYPE, EXPECTED_DOCUMENT_FILE_NAME, EXPECTED_DOCUMENT_SIZE, EXPECTED_DOCUMENT_CONTENT_TYPE),
                10l,
                15l);

    }

}
