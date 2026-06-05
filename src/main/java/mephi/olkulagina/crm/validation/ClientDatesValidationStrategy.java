package mephi.olkulagina.crm.validation;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ClientDatesValidationStrategy implements ValidationStrategy {
    private static final LocalDate MIN_DATE = LocalDate.of(2015, 1, 1);

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            LocalDate date = LocalDate.parse(value);
            return !date.isBefore(MIN_DATE);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Date must be on or after January 1, 2015";
    }
}