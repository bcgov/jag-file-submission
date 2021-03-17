package preview.ca.bc.gov.open.jag.efilingcsoclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import preview.ca.bc.gov.ag.csows.filing.status.DocumentType;
import preview.ca.bc.gov.ag.csows.filing.status.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingDocumentServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingDocumentService;
import preview.ca.bc.gov.ag.csows.filing.status.FilingStatusFacadeBean;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PreviewCsoDocumentServiceImpl implements EfilingDocumentService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FilingStatusFacadeBean previewFilingStatusFacadeBean;

    public PreviewCsoDocumentServiceImpl(FilingStatusFacadeBean previewFilingStatusFacadeBean) {
        this.previewFilingStatusFacadeBean = previewFilingStatusFacadeBean;
    }

    /**
     * <arg0>P</arg0> --> level
     * <arg1>F</arg1> --> class
     *
     * @param documentType
     * @return
     */
    @Override
    public ca.bc.gov.open.jag.efilingcommons.model.DocumentType getDocumentTypeDetails(String courtLevel, String courtClass, String documentType) {

        logger.warn("THIS IS IN PREVIEW MODE");

        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("courtLevel is required.");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("courtClass level is required.");
        if (StringUtils.isBlank(documentType)) throw new IllegalArgumentException("documentType level is required.");

        return getSoapDocumentTypes(courtLevel, courtClass).stream()
                .filter(doc -> doc.getDocumentTypeCd().equals(documentType))
                .findFirst()
                .map(doc -> new ca.bc.gov.open.jag.efilingcommons.model.DocumentType(doc.getDocumentTypeDesc(), doc.getDocumentTypeCd(), doc.getDefaultStatutoryFee(), doc.isOrderDocumentYn(),doc.isRushRequiredYn(), doc.isAutoProcessYn()))
                .orElseThrow(() -> new EfilingDocumentServiceException("Document type does not exists"));

    }

    public List<ca.bc.gov.open.jag.efilingcommons.model.DocumentType> getDocumentTypes(String courtLevel, String courtClass) {

        logger.warn("THIS IS IN PREVIEW MODE");

        if (StringUtils.isBlank(courtLevel)) throw new IllegalArgumentException("courtLevel is required.");
        if (StringUtils.isBlank(courtClass)) throw new IllegalArgumentException("courtClass level is required.");

        return getSoapDocumentTypes(courtLevel, courtClass).stream()
                .map(doc -> new ca.bc.gov.open.jag.efilingcommons.model.DocumentType(doc.getDocumentTypeDesc(), doc.getDocumentTypeCd(), doc.getDefaultStatutoryFee(), doc.isOrderDocumentYn(),doc.isRushRequiredYn(), doc.isAutoProcessYn())).collect(Collectors.toList());
    }

    private List<DocumentType> getSoapDocumentTypes(String courtLevel, String courtClass) {

        List<DocumentType> documentTypes = new ArrayList<>();

        try {
            documentTypes.addAll(previewFilingStatusFacadeBean.getDocumentTypes(courtLevel, courtClass));
        } catch (NestedEjbException_Exception e) {
            throw new EfilingDocumentServiceException("Exception while retrieving document details", e.getCause());
        }

        return documentTypes;

    }


}
