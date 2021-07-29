package com.amiron.booking.bot.exception;

import com.amiron.booking.core.property.LocalizedMessage;

/**
 * @author Aliaksandr Miron
 */
public class PhoneNumberInvalidFormatException extends RuntimeException {

    public static final String PHONE_NUMBER_INVALID_FORMAT_MESSAGE_KEY = "phone_number.invalid_format";

    @Override
    public String getLocalizedMessage() {
        return LocalizedMessage.getLocalizedMessage(PHONE_NUMBER_INVALID_FORMAT_MESSAGE_KEY);
    }
}
