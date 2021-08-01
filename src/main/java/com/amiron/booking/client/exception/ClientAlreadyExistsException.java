package com.amiron.booking.client.exception;

import com.amiron.booking.core.property.LocalizedMessage;

/**
 * @author Aliaksandr Miron
 */
public class ClientAlreadyExistsException extends RuntimeException {

    public static final String CLIENT_ALREADY_EXISTS_MESSAGE_KEY = "client.already_exists";

    @Override
    public String getLocalizedMessage() {
        return LocalizedMessage.getLocalizedMessage(CLIENT_ALREADY_EXISTS_MESSAGE_KEY);
    }
}
