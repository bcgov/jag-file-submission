package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.soap.service.EfilingDocumentService;

import java.math.BigDecimal;

public class EfilingDocumentServiceDemoImpl implements EfilingDocumentService {
    @Override
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        return new DocumentDetails("This is a doc", BigDecimal.valueOf(7));
    }
}
