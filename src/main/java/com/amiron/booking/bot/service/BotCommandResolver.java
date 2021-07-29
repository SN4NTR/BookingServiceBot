package com.amiron.booking.bot.service;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.BotCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.amiron.booking.bot.model.BotCommand.SET_EMAIL;
import static com.amiron.booking.bot.model.BotCommand.UNKNOWN;
import static com.amiron.booking.bot.validator.EmailValidator.EMAIL_REGEX;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class BotCommandResolver {

    public BotCommand resolveByCommandText(@NotNull final String commandText) {
        return BotCommand.findByValue(commandText).orElseThrow(CommandDoesNotExistException::new);
    }

    public BotCommand resolveByMessageText(@NotNull final String messageText) {
        return isEmail(messageText)
                ? SET_EMAIL
                : UNKNOWN;
    }

    private boolean isEmail(final String text) {
        return EMAIL_REGEX.matcher(text).matches();
    }
}
