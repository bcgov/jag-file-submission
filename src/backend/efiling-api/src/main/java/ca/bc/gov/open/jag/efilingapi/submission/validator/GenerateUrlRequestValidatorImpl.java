package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.*;
import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtFileNumberRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingapi.court.services.CourtService;
import ca.bc.gov.open.jag.efilingapi.document.DocumentService;
import ca.bc.gov.open.jag.efilingapi.document.models.GetValidDocumentTypesRequest;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageService;
import ca.bc.gov.open.jag.efilingapi.submission.models.GetValidPartyRoleRequest;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenerateUrlRequestValidatorImpl implements GenerateUrlRequestValidator {

    private final SubmissionService submissionService;
    private final CourtService courtService;
    private final DocumentService documentService;
    private final FilingPackageService filingPackageService;

    public GenerateUrlRequestValidatorImpl(SubmissionService submissionService,
                                           CourtService courtService,
                                           DocumentService documentService,
                                           FilingPackageService filingPackageService) {
        this.submissionService = submissionService;
        this.courtService = courtService;
        this.documentService = documentService;
        this.filingPackageService = filingPackageService;
    }

    @Override
    public Notification validate(GenerateUrlRequest generateUrlRequest, String applicationCode, String universalId) {

        Notification notification = new Notification();

        if (generateUrlRequest.getFilingPackage() == null) {
            notification.addError("Initial Package is required.");
            return notification;
        }

        notification.addError(validateNavigationUrls(generateUrlRequest.getNavigationUrls()));

        Optional<CourtDetails> courtDetails = this.courtService.getCourtDetails(GetCourtDetailsRequest
                .builder()
                .courtLocation(generateUrlRequest.getFilingPackage().getCourt().getLocation())
                .courtLevel(generateUrlRequest.getFilingPackage().getCourt().getLevel())
                .courtClassification(generateUrlRequest.getFilingPackage().getCourt().getCourtClass())
                .create());

        if(!courtDetails.isPresent()) {
            notification.addError(MessageFormat.format("Court with Location: [{0}], Level: [{1}], Classification: [{2}] is not a valid court.",
                    generateUrlRequest.getFilingPackage().getCourt().getLocation(), generateUrlRequest.getFilingPackage().getCourt().getLevel(), generateUrlRequest.getFilingPackage().getCourt().getCourtClass()));
            return notification;
        }

        notification.addError(validateCourt(generateUrlRequest.getFilingPackage(), courtDetails.get(), applicationCode));

        notification.addError(validateParties(generateUrlRequest.getFilingPackage()));

        if(!isNewSubmission(generateUrlRequest.getFilingPackage())) {
            notification.addError(validateCourtFileNumber(generateUrlRequest.getFilingPackage(), courtDetails.get(), applicationCode));
        }

        notification.addError(validateDocumentTypes(generateUrlRequest.getFilingPackage()));

        //Validate package number and document ids
        if (generateUrlRequest.getFilingPackage().getPackageNumber() != null) {
            notification.addError(validateActionsRequired(generateUrlRequest, universalId));
        }


        return notification;

    }

    private List<String> validateNavigationUrls(NavigationUrls navigationUrls) {

        List<String> result = new ArrayList<>();

        if(navigationUrls == null)   {
            result.add("Navigation Urls are required.");
            return result;
        }

        if(StringUtils.isBlank(navigationUrls.getError())) result.add("Error url is required.");
        if(StringUtils.isBlank(navigationUrls.getCancel())) result.add("Cancel url is required.");
        if(StringUtils.isBlank(navigationUrls.getSuccess())) result.add("Success url is required.");

        return result;

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
     * Party(Organization or Individual) types must be valid based on the document types submitted
     *
     * @param initialPackage
     */
    private List<String> validateParties(InitialPackage initialPackage) {

        if (StringUtils.isBlank(initialPackage.getCourt().getFileNumber()) && (CollectionUtils.emptyIfNull(initialPackage.getParties()).isEmpty() && CollectionUtils.emptyIfNull(initialPackage.getOrganizationParties()).isEmpty())) {
            return Arrays.asList("At least 1 party is required for new submission.");
        }

        List<String> validPartyRoles = submissionService.getValidPartyRoles(GetValidPartyRoleRequest
                .builder()
                .courtClassification(initialPackage.getCourt().getCourtClass())
                .courtLevel(initialPackage.getCourt().getLevel())
                .documents(initialPackage.getDocuments())
                .create());

        List<String> validationResult = new ArrayList<>();

        for (Individual individual : CollectionUtils.emptyIfNull(initialPackage.getParties())) {
            if (individual.getRoleType() == null || !validPartyRoles.contains(individual.getRoleType().toString())) {
                validationResult.add(MessageFormat.format("Individual role type [{0}] is invalid.", individual.getRoleType()));
            }
            if (StringUtils.isBlank(individual.getLastName())) {
                validationResult.add("Individual last name is required.");
            }
        }

        for (Organization organization : CollectionUtils.emptyIfNull(initialPackage.getOrganizationParties())) {
            if (organization.getRoleType() == null || !validPartyRoles.contains(organization.getRoleType().toString())) {
                validationResult.add(MessageFormat.format("Organization role type [{0}] is invalid.", organization.getRoleType()));
            }
            if (StringUtils.isBlank(organization.getName())) {
                validationResult.add("Organization name is required.");
            }
        }

        return validationResult;

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
                .create()).stream().map(DocumentTypeDetails::getType).collect(Collectors.toList());

        return initialPackage.getDocuments()
                .stream()
                .map(InitialDocument::getType)
                .filter(x -> !validDocumentTypes.contains(x))
                .map(invalidType -> MessageFormat.format("Document type [{0}] is invalid.", invalidType))
                .collect(Collectors.toList());
    }

    private List<String> validateActionsRequired(GenerateUrlRequest generateUrlRequest, String universalId) {

        List<String> result = new ArrayList<>();

        Optional<FilingPackage> filingPackage = filingPackageService.getCSOFilingPackage(universalId, generateUrlRequest.getFilingPackage().getPackageNumber());

        //Validate Package
        if (!filingPackage.isPresent()) {
            result.add("For given package number and universal id no record was found in cso");
            return result;
        }

        //Validate Documents
        if (generateUrlRequest.getFilingPackage().getDocuments().isEmpty()) {
            result.add("For given package there are no documents present");
        }

        return result;

    }

}
