package com.amiron.booking.bot.exception;

import com.amiron.booking.core.property.LocalizedMessage;

/**
 * @author Aliaksandr Miron
 */
public class EmailInvalidFormatException extends RuntimeException {

    public static final String EMAIL_INVALID_FORMAT_MESSAGE_KEY = "email.invalid_format";

    @Override
    public String getLocalizedMessage() {
        return LocalizedMessage.getLocalizedMessage(EMAIL_INVALID_FORMAT_MESSAGE_KEY);
    }
}
