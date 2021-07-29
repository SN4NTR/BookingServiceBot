package com.amiron.booking.core.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;

import static java.util.Objects.isNull;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class RequiredFieldValidator {

    public void validate(@Nullable final Object field) {
        throwIfNullOrBlank(field);
    }

    private void throwIfNullOrBlank(final Object field) {
        // TODO replace exception
        if (isNull(field) || field.toString().isBlank()) {
            throw new RuntimeException();
        }
    }
}
