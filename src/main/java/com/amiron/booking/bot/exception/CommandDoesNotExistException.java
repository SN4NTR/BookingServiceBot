package com.amiron.booking.bot.exception;

import com.amiron.booking.core.property.LocalizedMessage;

/**
 * @author Aliaksandr Miron
 */
public class CommandDoesNotExistException extends RuntimeException {

    public static final String COMMAND_DOES_NOT_EXIST_MESSAGE_KEY = "command.does_not_exist";

    @Override
    public String getLocalizedMessage() {
        return LocalizedMessage.getLocalizedMessage(COMMAND_DOES_NOT_EXIST_MESSAGE_KEY);
    }
}
