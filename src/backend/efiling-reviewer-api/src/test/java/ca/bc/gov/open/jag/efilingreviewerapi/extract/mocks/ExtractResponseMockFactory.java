package ca.bc.gov.open.jag.efilingreviewerapi.extract.mocks;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidation;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidationResult;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.ValidationTypes;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.Extract;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractResponse;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class ExtractResponseMockFactory {

    private static final String EXPECTED_DOCUMENT_CONTENT_TYPE = "application/pdf";
    private static final String EXPECTED_DOCUMENT_FILE_NAME = "myfile.pdf";
    private static final String EXPECTED_DOCUMENT_TYPE = "type";
    private static final BigDecimal EXPECTED_DOCUMENT_SIZE = BigDecimal.TEN;
    private static final UUID EXPECTED_EXTRACT_ID = UUID.fromString("89714a2a-611e-4f36-811b-1b2eb4cce7ee");
    private static final UUID EXPECTED_EXTRACT_TRANSACTION_ID = UUID.fromString("e40fb65d-1b61-40b9-9deb-d8eb3bab877f");
    private static final String EXPECTED = "TEST";
    private static final String ACTUAL = "TEST_ACTUAL";

    public static ExtractResponse mock() {

        return new ExtractResponse(
                new Extract(EXPECTED_EXTRACT_ID, EXPECTED_EXTRACT_TRANSACTION_ID),
                new Document(EXPECTED_DOCUMENT_TYPE, EXPECTED_DOCUMENT_FILE_NAME, EXPECTED_DOCUMENT_SIZE, EXPECTED_DOCUMENT_CONTENT_TYPE),
                new DocumentValidation(Collections.singletonList(new DocumentValidationResult(ValidationTypes.DOCUMENT_TYPE, EXPECTED, ACTUAL))),
                null);

    }

}
