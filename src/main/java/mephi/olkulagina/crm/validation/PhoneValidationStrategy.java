package mephi.olkulagina.crm.validation;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PhoneValidationStrategy implements ValidationStrategy {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{1,3} \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$");

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return PHONE_PATTERN.matcher(value).matches();
    }

    @Override
    public String getErrorMessage() {
        return "Invalid phone format. Expected: +Z (YYY) XXX-XX-XX";
    }
}