package ca.bc.gov.open.jag.efilingreviewerapi.document.validators;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.document.DocumentsApiDelegateImpl;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentTypeMismatchException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerRestrictedDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerVirusFoundException;
import ca.bc.gov.open.jag.efilingreviewerapi.utils.TikaAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentValidatorImpl implements DocumentValidator {

    Logger logger = LoggerFactory.getLogger(DocumentValidatorImpl.class);

    private final ClamAvService clamAvService;

    public DocumentValidatorImpl(ClamAvService clamAvService) {
        this.clamAvService = clamAvService;
    }

    @Override
    public void validateDocument(String documentType, MultipartFile file) {

        if (!Keys.ACCEPTED_DOCUMENT_TYPES.containsKey(documentType.toUpperCase())) throw new AiReviewerDocumentException("Invalid document type");

        try {
            clamAvService.scan(new ByteArrayInputStream(file.getBytes()));
            if (!TikaAnalysis.isPdf(file)) throw new AiReviewerDocumentException("Invalid file type");
        } catch (VirusDetectedException e) {
            throw new AiReviewerVirusFoundException("Virus found in document");
        } catch (IOException e) {
            throw new AiReviewerDocumentException("File is corrupt");
        }

    }

    @Override
    public void validateExtractedDocument(String documentType, List<DiligenAnswerField> answers) {

        Optional<String> returnedDocumentType = findDocumentType(answers);

        if (!returnedDocumentType.isPresent() || !returnedDocumentType.get().equals(Keys.ACCEPTED_DOCUMENT_TYPES.get(documentType))) {
            if (returnedDocumentType.isPresent() && Keys.RESTRICTED_DOCUMENT_TYPES.containsValue(returnedDocumentType.get())) {
                logger.error("A document of type {} detected.", returnedDocumentType.get());
                //TODO: delete document
                throw new AiReviewerRestrictedDocumentException("Document type mismatch detected");
            }
            logger.warn("A document of type {} was expected but {} was returned.", Keys.ACCEPTED_DOCUMENT_TYPES.get(documentType), returnedDocumentType);
            //Throw exception
            throw new AiReviewerDocumentTypeMismatchException("Document type mismatch detected");

        }
    }

    private Optional<String> findDocumentType(List<DiligenAnswerField> answers) {

        Optional<DiligenAnswerField> documentTypeAnswer = answers.stream()
                .filter(answer -> answer.getId().equals(Keys.ANSWER_DOCUMENT_TYPE_ID))
                .findFirst();

        if (!documentTypeAnswer.isPresent()) return Optional.empty();

        return documentTypeAnswer.get().getValues().stream().findFirst();

    }
}
