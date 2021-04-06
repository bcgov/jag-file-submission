package ca.bc.gov.open.jag.efilingreviewerapi.document.validators;

import ca.bc.gov.open.clamav.starter.ClamAvService;
import ca.bc.gov.open.clamav.starter.VirusDetectedException;
import ca.bc.gov.open.efilingdiligenclient.diligen.DiligenService;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.DiligenAnswerField;
import ca.bc.gov.open.jag.efilingreviewerapi.Keys;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidationResult;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentValidations;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.ValidationTypes;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerRestrictedDocumentException;
import ca.bc.gov.open.jag.efilingreviewerapi.error.AiReviewerVirusFoundException;
import ca.bc.gov.open.jag.efilingreviewerapi.utils.TikaAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentValidatorImpl implements DocumentValidator {

    Logger logger = LoggerFactory.getLogger(DocumentValidatorImpl.class);

    private final ClamAvService clamAvService;

    private final DiligenService diligenService;

    public DocumentValidatorImpl(ClamAvService clamAvService, DiligenService diligenService) {
        this.clamAvService = clamAvService;
        this.diligenService = diligenService;
    }

    @Override
    public void validateDocument(String documentType, MultipartFile file) {

        if (!Keys.ACCEPTED_DOCUMENT_TYPES.containsKey(documentType.toUpperCase())) {
            logger.error("A document of type {} is not valid", documentType);
            throw new AiReviewerDocumentException("Invalid document type");
        }

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
    public void validateExtractedDocument(BigDecimal documentId, String documentType, List<DiligenAnswerField> answers) {

        List<DocumentValidations> validationResults = new ArrayList<>();

        validationResults.add(validateDocumentType(documentId, documentType, answers).get());

        validationResults.addAll(validateParties(answers));

    }

    private Optional<DocumentValidations> validateDocumentType(BigDecimal documentId, String documentType, List<DiligenAnswerField> answers) {

        Optional<String> returnedDocumentType = findDocumentType(answers);

        if (!returnedDocumentType.isPresent() || !returnedDocumentType.get().equals(Keys.ACCEPTED_DOCUMENT_TYPES.get(documentType))) {
            if (returnedDocumentType.isPresent() && Keys.RESTRICTED_DOCUMENT_TYPES.containsValue(returnedDocumentType.get())) {
                logger.error("Document {} of type {} detected.", documentId, returnedDocumentType.get());
                diligenService.deleteDocument(documentId);
                logger.info("Document {} has been deleted.", documentId);
                throw new AiReviewerRestrictedDocumentException("Document type mismatch detected");
            }
            logger.warn("Document {} of type {} was expected but {} was returned.", documentId, Keys.ACCEPTED_DOCUMENT_TYPES.get(documentType), returnedDocumentType);

            return Optional.of(DocumentValidations.builder()
                    .actual((returnedDocumentType.orElse("No Document Found")))
                    .expected(Keys.ACCEPTED_DOCUMENT_TYPES.get(documentType))
                    .type(ValidationTypes.DOCUMENT_TYPE)
                    .create());
        }
        return Optional.empty();
    }

    private List<DocumentValidations> validateParties(List<DiligenAnswerField> answers) {

        List<DocumentValidations> validationResults = new ArrayList<>();

        if (findPartiesByType(answers, Keys.ANSWER_PLAINTIFF_ID).size() > 1) {
            validationResults.add(DocumentValidations.builder()
                    .actual(String.valueOf(findPartiesByType(answers, Keys.ANSWER_PLAINTIFF_ID).size()))
                    .expected("1")
                    .type(ValidationTypes.PARTIES_PLAINTIFF)
                    .create());
        }

        if (findPartiesByType(answers, Keys.ANSWER_DEFENDANT_ID).size() > 1) {
            validationResults.add(DocumentValidations.builder()
                    .actual(String.valueOf(findPartiesByType(answers, Keys.ANSWER_DEFENDANT_ID).size()))
                    .expected("1")
                    .type(ValidationTypes.PARTIES_DEFENDANT)
                    .create());
        }

        return validationResults;

    }

    private Optional<String> findDocumentType(List<DiligenAnswerField> answers) {

        Optional<DiligenAnswerField> documentTypeAnswer = answers.stream()
                .filter(answer -> answer.getId().equals(Keys.ANSWER_DOCUMENT_TYPE_ID))
                .findFirst();

        if (!documentTypeAnswer.isPresent()) return Optional.empty();

        return documentTypeAnswer.get().getValues().stream().findFirst();

    }

    private List<String> findPartiesByType(List<DiligenAnswerField> answers, Integer id) {

        Optional<DiligenAnswerField> documentTypeAnswer = answers.stream()
                .filter(answer -> answer.getId().equals(id))
                .findFirst();

        if (!documentTypeAnswer.isPresent()) return new ArrayList<>();

        return documentTypeAnswer.get().getValues().stream().collect(Collectors.toList());

    }
}
