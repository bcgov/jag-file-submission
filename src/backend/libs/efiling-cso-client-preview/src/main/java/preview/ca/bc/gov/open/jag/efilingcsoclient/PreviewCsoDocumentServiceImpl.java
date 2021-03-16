package preview.ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PreviewCsoDocumentServiceImpl implements EfilingDocumentService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public PreviewCsoDocumentServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean) {
        this.filingStatusFacadeBean = filingStatusFacadeBean;
    }

    /**
     * <arg0>P</arg0> --> level
     * <arg1>F</arg1> --> class
     *
     * @param documentType
     * @return
     */
    @Override
    public DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType) {

        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("courtLevel is required.");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("courtClass level is required.");
        if (StringUtils.isBlank(documentType)) throw new IllegalArgumentException("documentType level is required.");

        return getSoapDocumentTypes(courtLevel, courtClass).stream()
                .filter(doc -> doc.getDocumentTypeCd().equals(documentType))
                .findFirst()
                .map(doc -> new DocumentDetails(doc.getDocumentTypeDesc(), doc.getDefaultStatutoryFee(), doc.isOrderDocumentYn(), doc.isRushRequiredYn()))
                .orElseThrow(() -> new EfilingDocumentServiceException("Document type does not exists"));

    }

    public List<ca.bc.gov.open.jag.efilingcommons.model.DocumentType> getDocumentTypes(String courtLevel, String courtClass) {
        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("courtLevel is required.");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("courtClass level is required.");

        return getSoapDocumentTypes(courtLevel, courtClass).stream()
                .map(doc -> new ca.bc.gov.open.jag.efilingcommons.model.DocumentType(doc.getDocumentTypeDesc(), doc.getDocumentTypeCd(), doc.isRushRequiredYn())).collect(Collectors.toList());
    }

    private List<DocumentType> getSoapDocumentTypes(String courtLevel, String courtClass) {

        List<DocumentType> documentTypes = new ArrayList<>();

        try {
            documentTypes.addAll(filingStatusFacadeBean.getDocumentTypes(courtLevel, courtClass));
        } catch (NestedEjbException_Exception e) {
            throw new EfilingDocumentServiceException("Exception while retrieving document details", e.getCause());
        }

        return documentTypes;

    }


}
