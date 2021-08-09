package com.amiron.booking.bot.resolver;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.UserCommand;
import com.amiron.booking.bot.model.UserCommandPatternHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.amiron.booking.bot.model.UserCommand.SET_EMAIL;
import static com.amiron.booking.core.util.EmailUtils.isEmail;

/**
 * @author Aliaksandr Miron
 */
@Validated
@Component
public class UserCommandResolver {

    public UserCommand resolveByCommandText(@NotNull final String commandText) {
        return UserCommandPatternHolder.findCommandByTextCommand(commandText).orElseThrow(CommandDoesNotExistException::new);
    }

    public Optional<UserCommand> resolveByMessageText(@NotNull final String messageText) {
        return isEmail(messageText)
                ? Optional.of(SET_EMAIL)
                : Optional.empty();
    }
}
