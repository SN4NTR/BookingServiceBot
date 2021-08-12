package com.amiron.booking.bot.resolver;

import com.amiron.booking.bot.command.BotCommandPattern;
import com.amiron.booking.bot.exception.BotCommandDoesNotExistException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.amiron.booking.bot.command.BotCommandPattern.SET_EMAIL;
import static com.amiron.booking.bot.command.BotCommandPattern.findBotPatternByTextCommand;
import static com.amiron.booking.core.util.EmailUtils.isEmail;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class BotCommandPatternResolver {

    public BotCommandPattern resolveByCommandText(@NotNull final String textCommand) {
        return findBotPatternByTextCommand(textCommand).orElseThrow(BotCommandDoesNotExistException::new);
    }

    public Optional<BotCommandPattern> resolveByMessageText(@NotNull final String messageText) {
        return isEmail(messageText)
                ? Optional.of(SET_EMAIL)
                : Optional.empty();
    }
}
