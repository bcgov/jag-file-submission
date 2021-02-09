package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialDocument;
import ca.bc.gov.open.jag.efilingapi.submission.models.GetValidPartyRoleRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetValidPartyRoleRequestTest {

    public static final String COURT_CLASSIFICATION = "A";
    public static final String COURT_LEVEL = "B";

    @Test
    @DisplayName("ok: returns a comma separated list of document types")
    public void shouldReturnAValidListOfDocumentTypes() {

        List<InitialDocument> initialDocumentList = new ArrayList<>();
        InitialDocument document1 = new InitialDocument();
        DocumentProperties documentProperties1 = new DocumentProperties();
        documentProperties1.setType(DocumentProperties.TypeEnum.AAB);
        document1.setDocumentProperties(documentProperties1);
        initialDocumentList.add(document1);
        InitialDocument document2 = new InitialDocument();
        DocumentProperties documentProperties2 = new DocumentProperties();
        documentProperties2.setType(DocumentProperties.TypeEnum.ACMW);
        document2.setDocumentProperties(documentProperties2);
        initialDocumentList.add(document2);
        InitialDocument document3 = new InitialDocument();
        DocumentProperties documentProperties3 = new DocumentProperties();
        documentProperties3.setType(DocumentProperties.TypeEnum.TAX);
        document3.setDocumentProperties(documentProperties3);
        initialDocumentList.add(document3);

        GetValidPartyRoleRequest actual = GetValidPartyRoleRequest.builder()
                .courtClassification(COURT_CLASSIFICATION)
                .courtLevel(COURT_LEVEL)
                .documents(initialDocumentList).create();

        Assertions.assertEquals(COURT_CLASSIFICATION, actual.getCourtClassification());
        Assertions.assertEquals(COURT_LEVEL, actual.getCourtLevel());
        Assertions.assertEquals(3, actual.getInitialDocuments().size());
        Assertions.assertEquals("AAB,ACMW,TAX", actual.getDocumentTypesAsString());


    }


}
