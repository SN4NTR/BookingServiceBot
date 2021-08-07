package com.amiron.booking.bot.resolver;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.UserCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.UserCommand.SET_EMAIL;
import static com.amiron.booking.bot.model.UserCommand.UNKNOWN;
import static com.amiron.booking.bot.util.CommandUtils.isCommandWithDate;
import static com.amiron.booking.bot.util.CommandUtils.isCommandWithUuid;
import static com.amiron.booking.bot.util.CommandUtils.removeDatePartFromCommand;
import static com.amiron.booking.bot.util.CommandUtils.removeUuidPartFromCommand;
import static com.amiron.booking.core.util.EmailUtils.isEmail;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class UserCommandResolver {

    public UserCommand resolveByCommandText(@NotNull final String commandText) {
        final String pureCommandText = removeSecondaryPartFromCommandIfNeeded(commandText);
        return UserCommand.findByValue(pureCommandText).orElseThrow(CommandDoesNotExistException::new);
    }

    public UserCommand resolveByMessageText(@NotNull final String messageText) {
        return isEmail(messageText)
                ? SET_EMAIL
                : UNKNOWN;
    }

    private String removeSecondaryPartFromCommandIfNeeded(final String commandText) {
        if (isCommandWithUuid(commandText)) {
            removeUuidPartFromCommand(commandText);
        } else if (isCommandWithDate(commandText)) {
            removeDatePartFromCommand(commandText);
        }
        return commandText;
    }
}
