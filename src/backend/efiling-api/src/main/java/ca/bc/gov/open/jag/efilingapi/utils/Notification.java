package ca.bc.gov.open.jag.efilingapi.utils;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private List<String> errors = new ArrayList<>();

    public Boolean hasError() {
        return !this.errors.isEmpty();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addError(List<String> errors) {
        this.errors.addAll(errors);
    }

    public List<String> getErrors() {
        return this.errors;
    }


}
