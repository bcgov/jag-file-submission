package ca.bc.gov.open.jag.efilingaccountclient;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CSODocumentServiceImpl implements EfilingDocumentService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CSODocumentServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
    }

    /**
     * <arg0>P</arg0> --> level
     * <arg1>F</arg1> --> class
     * @param documentType
     * @return
     */
    @Override
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {
        try {
            List<DocumentType> documentTypes = filingStatusFacadeBean.getDocumentTypes(courtLevel, courtClass);

            if(documentTypes == null) return null;

            Optional<DocumentDetails> documentDetails = documentTypes.stream()
                    .filter(doc -> doc.getDocumentTypeCd().equals(documentType))
                    .findFirst()
                    .map(doc -> new DocumentDetails(doc.getDocumentTypeDesc(), doc.getDefaultStatutoryFee()));

            if(!documentDetails.isPresent())
                throw new EfilingDocumentServiceException("Document type does not exists");

            return documentDetails.get();

        } catch (NestedEjbException_Exception e) {
            throw new EfilingDocumentServiceException("Exception while retrieving document details", e.getCause());
        }
    }
}
