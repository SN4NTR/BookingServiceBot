package com.amiron.booking.bot.validator;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.UserCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class CommandExistenceValidator {

    public void validate(final String command) {
        throwIfDoesNotExist(command);
    }

    private void throwIfDoesNotExist(final String command) {
        if (UserCommand.doesNotContainValue(command)) {
            throw new CommandDoesNotExistException();
        }
    }
}
