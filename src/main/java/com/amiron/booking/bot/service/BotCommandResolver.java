package com.amiron.booking.bot.service;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.BotCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.BotCommand.SET_EMAIL;
import static com.amiron.booking.bot.model.BotCommand.UNKNOWN;
import static com.amiron.booking.bot.util.CommandUtils.isCommandWithUuid;
import static com.amiron.booking.bot.util.CommandUtils.removeUuidPartFromCommand;
import static com.amiron.booking.bot.util.EmailUtils.isEmail;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class BotCommandResolver {

    public BotCommand resolveByCommandText(@NotNull final String commandText) {
        final String pureCommandText = removeUuidFromCommandIfNeeded(commandText);
        return BotCommand.findByValue(pureCommandText).orElseThrow(CommandDoesNotExistException::new);
    }

    public BotCommand resolveByMessageText(@NotNull final String messageText) {
        return isEmail(messageText)
                ? SET_EMAIL
                : UNKNOWN;
    }

    private String removeUuidFromCommandIfNeeded(final String commandText) {
        return isCommandWithUuid(commandText)
                ? removeUuidPartFromCommand(commandText)
                : commandText;
    }
}
