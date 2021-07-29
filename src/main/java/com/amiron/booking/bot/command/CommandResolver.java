package com.amiron.booking.bot.command;

import com.amiron.booking.bot.exception.CommandDoesNotExistException;
import com.amiron.booking.bot.model.BotCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Aliaksandr Miron
 */
@AllArgsConstructor
@Validated
@Service
public class CommandResolver {

    private final CommandsContextHolder commandsContextHolder;

    public Command resolve(@NotNull final BotCommand botCommand) {
        return commandsContextHolder.findByCommand(botCommand).orElseThrow(CommandDoesNotExistException::new);
    }
}
