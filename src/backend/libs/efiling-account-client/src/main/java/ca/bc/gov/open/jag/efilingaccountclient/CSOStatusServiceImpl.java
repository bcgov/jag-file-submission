package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSOStatusServiceImpl implements EfilingDocumentService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CSOStatusServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
    }

    @Override
    public DocumentDetails getDocumentDetails(String documentType) {
        try {
            List<DocumentType> documentTypes = filingStatusFacadeBean.getDocumentTypes("", "");
        } catch (NestedEjbException_Exception e) {
            throw new EfilingLookupServiceException("Exception while retrieving service fee", e.getCause());
        }
        return null;
    }
}
