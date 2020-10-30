package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;
import org.apache.commons.lang3.StringUtils;

public class GenerateUrlRequestValidatorImpl implements GenerateUrlRequestValidator {

    public Notification validate(GenerateUrlRequest initialPackage) {

        Notification notification = new Notification();

        if(initialPackage.getFilingPackage() == null) {
            notification.addError("Initial Package is required.");
            return notification;
        }

        validateParties(initialPackage.getFilingPackage(), notification);

        return notification;

    }

    private void validateParties(InitialPackage initialPackage, Notification notification) {

        if(StringUtils.isBlank(initialPackage.getCourt().getFileNumber())) {

            if(initialPackage.getParties().isEmpty()) {
                notification.addError("At least 1 party is required for new submission.");
            }
        }

    }

}
