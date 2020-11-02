package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.Party;
import ca.bc.gov.open.jag.efilingapi.submission.service.GetValidPartyRoleRequest;
import ca.bc.gov.open.jag.efilingapi.submission.service.SubmissionService;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateUrlRequestValidatorImpl implements GenerateUrlRequestValidator {

    private final SubmissionService submissionService;

    public GenerateUrlRequestValidatorImpl(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    public Notification validate(GenerateUrlRequest initialPackage) {

        Notification notification = new Notification();

        if (initialPackage.getFilingPackage() == null) {
            notification.addError("Initial Package is required.");
            return notification;
        }

        validateParties(initialPackage.getFilingPackage(), notification);

        return notification;

    }

    /**
     * Validates parties
     *
     * if court file number is empty then at least 1 party is required
     *
     * Parties types must be valid based on the document types submitted
     *
     * @param initialPackage
     * @param notification
     */
    private void validateParties(InitialPackage initialPackage, Notification notification) {

        if (StringUtils.isBlank(initialPackage.getCourt().getFileNumber()) && initialPackage.getParties().isEmpty()) {
            notification.addError("At least 1 party is required for new submission.");
            return;
        }

        List<String> validPartyRoles = submissionService.getValidPartyRoles(GetValidPartyRoleRequest
                .builder()
                .courtClassification(initialPackage.getCourt().getCourtClass())
                .courtLevel(initialPackage.getCourt().getLevel())
                .documents(initialPackage.getDocuments())
                .create());

        List<Party> invalidParties = initialPackage
                .getParties()
                .stream()
                .filter(party -> party.getRoleType() == null || !validPartyRoles.contains(party.getRoleType().toString()))
                .collect(Collectors.toList());

        if(!invalidParties.isEmpty()) {

            invalidParties.stream().forEach(party -> {
                notification.addError(MessageFormat.format("Role type [{0}] is invalid.", party.getRoleType()));
            });

        }

    }

}
