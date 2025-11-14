package ca.bc.gov.open.jag.efilingcsoclient;

import ca.bc.gov.ag.csows.filing.status.DocumentType;
import ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.bc.gov.open.jag.efilingcsoclient.Keys.OTHER_DOCUMENT_TYPE;


public class CsoDocumentServiceImpl implements EfilingDocumentService {

    private final FilingStatusFacadeBean filingStatusFacadeBean;

    public CsoDocumentServiceImpl(FilingStatusFacadeBean filingStatusFacadeBean) {
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
    public DocumentTypeDetails getDocumentTypeDetails(String courtLevel, String courtClass, String documentType, String division) {

        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("courtLevel is required.");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("courtClass level is required.");
        if (StringUtils.isBlank(documentType)) throw new IllegalArgumentException("documentType level is required.");

        return getSoapDocumentTypes(courtLevel, courtClass, division).stream()
                .filter(doc -> doc.getDocumentTypeCd().equals(documentType))
                .findFirst()
                .map(doc -> new DocumentTypeDetails(doc.getDocumentTypeDesc(), doc.getDocumentTypeCd(), doc.getDefaultStatutoryFee(), doc.isOrderDocumentYn(),doc.isRushRequiredYn(), doc.isAutoProcessYn()))
                .orElseThrow(() -> new EfilingDocumentServiceException("Document type does not exists"));

    }

    public List<DocumentTypeDetails> getDocumentTypes(String courtLevel, String courtClass, String division) {

        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("courtLevel is required.");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("courtClass level is required.");

        return getSoapDocumentTypes(courtLevel, courtClass, division).stream()
                .map(doc -> new DocumentTypeDetails(doc.getDocumentTypeDesc(), doc.getDocumentTypeCd(), doc.getDefaultStatutoryFee(), doc.isOrderDocumentYn(),doc.isRushRequiredYn(), doc.isAutoProcessYn())).collect(Collectors.toList());

    }

    private List<DocumentType> getSoapDocumentTypes(String courtLevel, String courtClass, String division) {

        List<DocumentType> documentTypes;

        try {
            documentTypes = new ArrayList<>(filingStatusFacadeBean.getDocumentTypes(division, courtLevel, courtClass));
            //Remove other document types. NOTE these are cached and redis requires a restart
            //TODO: consider feature flagging this
            documentTypes.removeIf(documentType -> documentType.getDocumentTypeCd().equals(OTHER_DOCUMENT_TYPE));
        } catch (NestedEjbException_Exception e) {
            throw new EfilingDocumentServiceException("Exception while retrieving document details", e.getCause());
        }

        return documentTypes;

    }


}
