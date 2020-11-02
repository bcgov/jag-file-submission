package ca.bc.gov.open.jag.efilingapi.submission.service;

import ca.bc.gov.open.jag.efilingapi.api.model.DocumentProperties;
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

        List<DocumentProperties> documentPropertiesList = new ArrayList<>();
        DocumentProperties document1 = new DocumentProperties();
        document1.setType(DocumentProperties.TypeEnum.AAB);
        documentPropertiesList.add(document1);
        DocumentProperties document2 = new DocumentProperties();
        document2.setType(DocumentProperties.TypeEnum.ACMW);
        documentPropertiesList.add(document2);
        DocumentProperties document3 = new DocumentProperties();
        document3.setType(DocumentProperties.TypeEnum.TAX);
        documentPropertiesList.add(document3);

        GetValidPartyRoleRequest actual = GetValidPartyRoleRequest.builder()
                .courtClassification(COURT_CLASSIFICATION)
                .courtLevel(COURT_LEVEL)
                .documents(documentPropertiesList).create();

        Assertions.assertEquals(COURT_CLASSIFICATION, actual.getCourtClassification());
        Assertions.assertEquals(COURT_LEVEL, actual.getCourtLevel());
        Assertions.assertEquals(3, actual.getDocumentPropertiesList().size());
        Assertions.assertEquals("AAB,ACMW,TAX", actual.getDocumentTypesAsString());


    }


}
