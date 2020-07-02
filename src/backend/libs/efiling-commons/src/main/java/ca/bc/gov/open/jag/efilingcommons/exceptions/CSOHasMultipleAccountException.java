package ca.bc.gov.open.jag.efilingcommons.exceptions;

import java.text.MessageFormat;

public class CSOHasMultipleAccountException extends EfilingAccountServiceException {

    public CSOHasMultipleAccountException(String clientId) {

        super(MessageFormat.format("Client {0} has multiple CSO profiles", clientId));
    }
}
