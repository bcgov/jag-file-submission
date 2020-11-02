package ca.bc.gov.open.jag.efilingapi.utils;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private List<String> errors = new ArrayList<>();

    public Boolean hasError() {
        return !errors.isEmpty();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

}
