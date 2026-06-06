package mephi.olkulagina.crm.validation;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ClientDatesValidationStrategy implements ValidationStrategy {
    private static final int MIN_YEAR = 2015;
    private static final int MIN_MONTH = 1;
    private static final int MIN_DAY = 1;
    private static final LocalDate MIN_DATE = LocalDate.of(MIN_YEAR, MIN_MONTH, MIN_DAY);

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
        return "Date must be on or after January 1, " + MIN_YEAR;
    }
}