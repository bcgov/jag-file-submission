package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSOStatusServiceImpl implements EfilingDocumentService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CSOStatusServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
    }

    @Override
    public DocumentDetails getDocumentDetails(String documentType) {
        try {
            //TODO: we need to find out what arg0 and arg1 represent
            List<DocumentType> documentTypes = filingStatusFacadeBean.getDocumentTypes("", "");
            Optional<DocumentDetails> documentDetails = documentTypes.stream()
                    .filter(doc -> doc.getDocumentTypeCd() == documentType)
                    .map(doc -> new DocumentDetails(doc.getDocumentTypeDesc(), doc.getDefaultStatutoryFee()))
                    .findFirst();
            return documentDetails.orElse(null);
        } catch (NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while retrieving service fee", e.getCause());
        }
    }
}
