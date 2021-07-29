package com.amiron.booking.bot.validator;

import com.amiron.booking.bot.exception.EmailInvalidFormatException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class EmailValidator {

    public static final Pattern EMAIL_REGEX = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$");

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
