package ca.bc.gov.open.jag.efilingreviewerapi.extract;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.exception.DiligenDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.api.DocumentsApiDelegate;
import ca.bc.gov.open.jag.efilingreviewerapi.utils.TikaAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
@Service
public class DocumentsApiDelegateImpl implements DocumentsApiDelegate {

    Logger logger = LoggerFactory.getLogger(DocumentsApiDelegateImpl.class);

    private final DiligenService diligenService;

    private final ClamAvService clamAvService;

    public DocumentsApiDelegateImpl(DiligenService diligenService, ClamAvService clamAvService) {
        this.diligenService = diligenService;
        this.clamAvService = clamAvService;
    }

    @Override
    public ResponseEntity<Object> extractDocumentFormData(UUID xTransactionId, String xDocumentType, MultipartFile file) {

        logger.info("document extract request received");

        try {
            clamAvService.scan(new ByteArrayInputStream(file.getBytes()));
        } catch (VirusDetectedException e) {
            throw new DiligenDocumentException("Virus found in document");
        } catch (IOException e) {
            throw new DiligenDocumentException("File is corrupt");
        }

        if (!TikaAnalysis.isPdf(file)) throw new DiligenDocumentException("Invalid file type");

        return ResponseEntity.ok(diligenService.postDocument(xDocumentType, file));

    }
}
