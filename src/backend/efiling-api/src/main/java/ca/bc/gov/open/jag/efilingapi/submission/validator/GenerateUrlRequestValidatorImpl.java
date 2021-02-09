package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtFileNumberRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingapi.submission.models.GetValidPartyRoleRequest;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateUrlRequestValidatorImpl implements GenerateUrlRequestValidator {

    private final SubmissionService submissionService;
    private final CourtService courtService;
    private final DocumentService documentService;

    public GenerateUrlRequestValidatorImpl(SubmissionService submissionService,
                                           CourtService courtService, DocumentService documentService) {
        this.submissionService = submissionService;
        this.courtService = courtService;
        this.documentService = documentService;
    }

    @Override
    public Notification validate(GenerateUrlRequest generateUrlRequest, String applicationCode) {

        Notification notification = new Notification();

        if (generateUrlRequest.getFilingPackage() == null) {
            notification.addError("Initial Package is required.");
            return notification;
        }

        CourtDetails courtDetails = this.courtService.getCourtDetails(GetCourtDetailsRequest
                .builder()
                .courtLocation(generateUrlRequest.getFilingPackage().getCourt().getLocation())
                .courtLevel(generateUrlRequest.getFilingPackage().getCourt().getLevel())
                .courtClassification(generateUrlRequest.getFilingPackage().getCourt().getCourtClass())
                .create());

        if(courtDetails == null) {
            notification.addError(MessageFormat.format("Court with Location: [{0}], Level: [{1}], Classification: [{2}] is not a valid court.",
                    generateUrlRequest.getFilingPackage().getCourt().getLocation(), generateUrlRequest.getFilingPackage().getCourt().getLevel(), generateUrlRequest.getFilingPackage().getCourt().getCourtClass()));
            return notification;
        }

        notification.addError(validateCourt(generateUrlRequest.getFilingPackage(), courtDetails, applicationCode));

        notification.addError(validateParties(generateUrlRequest.getFilingPackage()));

        if(!isNewSubmission(generateUrlRequest.getFilingPackage())) {
            notification.addError(validateCourtFileNumber(generateUrlRequest.getFilingPackage(), courtDetails, applicationCode));
        }

        notification.addError(validateDocumentTypes(generateUrlRequest.getFilingPackage()));

        return notification;

    }

    private List<String> validateCourt(InitialPackage initialPackage, CourtDetails courtDetails, String applicationCode) {

        if (!this.courtService.isValidCourt(IsValidCourtRequest
                .builder()
                .applicationCode(applicationCode)
                .courtClassification(initialPackage.getCourt().getCourtClass())
                .courtLevel(initialPackage.getCourt().getLevel())
                .courtId(courtDetails.getCourtId())
                .create()))
             return Arrays.asList(MessageFormat.format("Court with Location: [{0}], Level: [{1}], Classification: [{2}] is not a valid court.",
                    initialPackage.getCourt().getLocation(), initialPackage.getCourt().getLevel(), initialPackage.getCourt().getCourtClass()));

        return new ArrayList<>();

    }

    private boolean isNewSubmission(InitialPackage initialPackage) {
        return initialPackage.getCourt() != null && StringUtils.isBlank(initialPackage.getCourt().getFileNumber());
    }

    /**
     * Validates parties
     *
     * if court file number is empty then at least 1 party is required
     *
     * Party types must be valid based on the document types submitted
     *
     * @param initialPackage
     */
    private List<String> validateParties(InitialPackage initialPackage) {

        if (StringUtils.isBlank(initialPackage.getCourt().getFileNumber()) && initialPackage.getParties().isEmpty()) {
            return Arrays.asList("At least 1 party is required for new submission.");
        }

        List<String> validPartyRoles = submissionService.getValidPartyRoles(GetValidPartyRoleRequest
                .builder()
                .courtClassification(initialPackage.getCourt().getCourtClass())
                .courtLevel(initialPackage.getCourt().getLevel())
                .documents(initialPackage.getDocuments())
                .create());

        return initialPackage
                .getParties()
                .stream()
                .filter(party -> party.getRoleType() == null || !validPartyRoles.contains(party.getRoleType().toString()))
                .map(party -> MessageFormat.format("Role type [{0}] is invalid.", party.getRoleType()))
                .collect(Collectors.toList());

    }

    private List<String> validateCourtFileNumber(InitialPackage initialPackage, CourtDetails courtDetails, String applicationCode) {

        List<String> result = new ArrayList<>();

        if(!this.courtService.isValidCourtFileNumber(IsValidCourtFileNumberRequest
                .builder()
                .fileNumber(initialPackage.getCourt().getFileNumber())
                .courtClassification(initialPackage.getCourt().getCourtClass())
                .courtLevel(initialPackage.getCourt().getLevel())
                .courtId(courtDetails.getCourtId())
                .applicationCode(applicationCode)
                .create()))
            result.add(MessageFormat.format("FileNumber [{0}] does not exists.", initialPackage.getCourt().getFileNumber()));

        return result;

    }

    public List<String> validateDocumentTypes(InitialPackage initialPackage) {

        List<String> validDocumentTypes = this.documentService.getValidDocumentTypes(GetValidDocumentTypesRequest.builder()
                .courtClassification(initialPackage.getCourt().getCourtClass())
                .courtLevel(initialPackage.getCourt().getLevel())
                .create()).stream().map(x -> x.getType()).collect(Collectors.toList());

        return initialPackage.getDocuments()
                .stream()
                .map(x -> x.getDocumentProperties().getType().getValue())
                .filter(x -> !validDocumentTypes.contains(x))
                .map(invalidType -> MessageFormat.format("Document type [{0}] is invalid.", invalidType))
                .collect(Collectors.toList());
    }

}
