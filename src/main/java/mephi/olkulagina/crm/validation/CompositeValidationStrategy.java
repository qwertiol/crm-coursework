package mephi.olkulagina.crm.validation;

import java.util.ArrayList;
import java.util.List;

public class CompositeValidationStrategy {

    private final List<ValidationStrategy> validators;

    public CompositeValidationStrategy(List<ValidationStrategy> validators) {
        this.validators = validators;
    }

    public List<String> validate(String fieldName, String value) {
        List<String> errors = new ArrayList<>();
        for (ValidationStrategy validator : validators) {
            if (!validator.isValid(value)) {
                errors.add("Field '" + fieldName + "': " + validator.getErrorMessage());
            }
        }
        return errors;
    }

    public boolean hasErrors(List<String> errors) {
        return !errors.isEmpty();
    }
}