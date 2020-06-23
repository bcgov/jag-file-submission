package ca.bc.gov.open.jag.efilingaccountclient.exception;

import java.text.MessageFormat;

public class CSOHasMultipleAccountException extends RuntimeException {

    public CSOHasMultipleAccountException(String clientId) {

        super(MessageFormat.format("Client {0} has multiple CSO profiles", clientId));
    }
}
