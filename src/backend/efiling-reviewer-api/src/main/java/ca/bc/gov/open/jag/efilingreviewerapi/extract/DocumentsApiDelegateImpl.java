package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.api.model.Extract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;
@Service
public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    private final DiligenService diligenService;

    public DocumentsApiDelegateImpl(DiligenService diligenService) {
        this.diligenService = diligenService;
    }

    @Override
    public ResponseEntity<DocumentExtractResponse> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        logger.info("document extract request received");

        BigDecimal response = diligenService.postDocument(xDocumentType, file);

        DocumentExtractResponse documentExtractResponse = new DocumentExtractResponse();

        Document document = new Document();
        document.setFileName(file.getName());
        document.setType(xDocumentType);
        document.setSize(new BigDecimal(file.getSize()));
        document.setContentType(file.getContentType());

        documentExtractResponse.setDocument(document);
        Extract extract = new Extract();
        extract.setId(UUID.randomUUID());
        extract.setTransactioniId(xTransactionId);
        documentExtractResponse.setExtract(extract);

        return ResponseEntity.ok(documentExtractResponse);

    }
}
