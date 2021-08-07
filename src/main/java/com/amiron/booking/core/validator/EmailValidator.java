package com.amiron.booking.core.validator;

import com.amiron.booking.core.exception.EmailInvalidFormatException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.core.util.EmailUtils.EMAIL_REGEX;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class EmailValidator {

    public void validate(@NotNull final String email) {
        throwIfPatternDoesNotMatch(email);
    }

    private void throwIfPatternDoesNotMatch(final String email) {
        if (!isPatternMatched(email)) {
            throw new EmailInvalidFormatException();
        }
    }

    private boolean isPatternMatched(final String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }
}
