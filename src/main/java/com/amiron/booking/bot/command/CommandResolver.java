package com.amiron.booking.bot.command;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.BotCommand;
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
public class CommandResolver<T extends BotApiObject> {

    private final CommandsContextHolder<T> commandsContextHolder;

    public Command<T> resolve(@NotNull final BotCommand botCommand) {
        return commandsContextHolder.findByCommand(botCommand).orElseThrow(CommandDoesNotExistException::new);
    }
}
