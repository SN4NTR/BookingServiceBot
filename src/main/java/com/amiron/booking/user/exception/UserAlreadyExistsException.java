package com.amiron.booking.user.exception;

import com.amiron.booking.core.property.LocalizedMessage;

/**
 * @author Aliaksandr Miron
 */
public class UserAlreadyExistsException extends RuntimeException {

    public static final String USER_ALREADY_EXISTS_MESSAGE_KEY = "user.already_exists";

    @Override
    public String getLocalizedMessage() {
        return LocalizedMessage.getLocalizedMessage(USER_ALREADY_EXISTS_MESSAGE_KEY);
    }
}
