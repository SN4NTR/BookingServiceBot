package com.amiron.booking.core.validator;

import com.amiron.booking.core.exception.PhoneNumberInvalidFormatException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class PhoneNumberValidator {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d+");

    public void validate(@NotNull final String phoneNumber) {
        throwIfPatternDoesNotMatch(phoneNumber);
    }

    private void throwIfPatternDoesNotMatch(final String phoneNumber) {
        if (!isPatternMatched(phoneNumber)) {
            throw new PhoneNumberInvalidFormatException();
        }
    }

    private boolean isPatternMatched(final String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }
}
