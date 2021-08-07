package com.amiron.booking.bot.resolver;

import com.amiron.booking.bot.command.BotCommand;
import com.amiron.booking.bot.command.CommandsContextHolder;
import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.UserCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class BotCommandResolver<T extends BotApiObject> {

    private final CommandsContextHolder<T> commandsContextHolder;

    public BotCommand<T> resolve(@NotNull final UserCommand userCommand) {
        return commandsContextHolder.findByCommand(userCommand).orElseThrow(CommandDoesNotExistException::new);
    }
}
