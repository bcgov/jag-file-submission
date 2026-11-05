package ca.bc.gov.open.jag.efilingapi.submission.validator;

import ca.bc.gov.open.jag.efilingapi.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilingapi.utils.Notification;

public interface GenerateUrlRequestValidator {

    Notification validate(GenerateUrlRequest generateUrlRequest, String applicationCode, String universalId);

}
