package mephi.olkulagina.crm.validation;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class EmailValidationStrategy implements ValidationStrategy {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }

    @Override
    public String getErrorMessage() {
        return "Invalid email format";
    }
}