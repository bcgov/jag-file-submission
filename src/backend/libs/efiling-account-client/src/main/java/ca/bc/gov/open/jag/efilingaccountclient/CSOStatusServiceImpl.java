package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.stereotype.Service;

@Service
public class CSOStatusServiceImpl implements EfilingDocumentService {

    @Override
    public DocumentDetails getDocumentDetails(String documentType) {
        return null;
    }
}
